package com.idgovern.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.idgovern.dao.SysAclMapper;
import com.idgovern.dao.SysAclModuleMapper;
import com.idgovern.dao.SysDeptMapper;
import com.idgovern.dto.AclDto;
import com.idgovern.dto.AclModuleLevelDto;
import com.idgovern.dto.DeptLevelDto;
import com.idgovern.model.SysAcl;
import com.idgovern.model.SysAclModule;
import com.idgovern.model.SysDept;
import com.idgovern.util.LevelUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service responsible for constructing hierarchical tree structures
 * for Departments, ACL Modules, and Permission Points.
 *
 * <p>
 * This class transforms flat database result sets into tree-structured
 * data representations for frontend display and RBAC permission management.
 * </p>
 *
 * <p>
 * Supports:
 * <ul>
 *     <li>Department tree generation</li>
 *     <li>ACL module tree generation</li>
 *     <li>User permission tree</li>
 *     <li>Role permission assignment tree</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-16 | Lilian S.| Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */

@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysCoreService sysCoreService;

    @Resource
    private SysAclMapper sysAclMapper;

    /**
     * Builds a permission tree for a specific user. 2016-02-16
     *
     * @param userId user identifier
     * @return ACL module tree with user permissions marked as checked
     */
    public List<AclModuleLevelDto> userAclTree(int userId) {

        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();

        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }

        return aclListToTree(aclDtoList);
    }


    /**
     * Builds a permission tree for role assignment.
     * It's the entry point to gather all the raw data and "mark" each permission with two specific flags.
     *
     * <p>
     * Marks:
     * - Permissions current user has access to (hasAcl)
     * - Permissions assigned to the role (checked)
     * </p>
     *
     * @param roleId role identifier. It gets the permissions the current user has (for security/visibility)
     *               and the permissions the target role has.
     * @return hierarchical ACL module tree
     */
    public List<AclModuleLevelDto> roleTree(int roleId) {

        // 1. Permissions assigned to current user
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();

        // 2. Permissions assigned to the target role
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);

//        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
//        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        Set<Integer> userAclIdSet = userAclList.stream()
                .map(SysAcl::getId)
                .collect(Collectors.toSet());

        Set<Integer> roleAclIdSet = roleAclList.stream()
                .map(SysAcl::getId)
                .collect(Collectors.toSet());


        // 3、All permissions
        List<AclDto> aclDtoList = Lists.newArrayList();
        List<SysAcl> allAclList = sysAclMapper.getAll();

        for (SysAcl acl : allAclList) {

            AclDto dto = AclDto.adapt(acl);

            if (userAclIdSet.contains(acl.getId())) {
                // Sets Flags: It loops through every single permission in the system.
                dto.setHasAcl(true);
            }

            if (roleAclIdSet.contains(acl.getId())) {
                // Sets Flags: It loops through every single permission in the system.
                dto.setChecked(true);
            }

            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }


    /**
     * The Data Organizer
     * Converts a flat ACL list into a hierarchical ACL module tree.
     */
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {

        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }

        //Fetches the Module Tree: a helper that builds the parent-child relationships between modules themselves
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        // Groups by Module
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for(AclDto acl : aclDtoList) {
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }

        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }


    /**
     * The Recursive Builder. It navigates through the modules and sticks the permissions into the right spots.
     * Recursively binds ACL permissions to corresponding modules.
     *
     */
    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, AclDto> moduleIdAclMap) {

        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            // Base Case: If the module list is empty, it stops.
            return;
        }

        for (AclModuleLevelDto dto : aclModuleLevelList) {
            // Retrieve all active permission entries under the current module.
            // Any permission that is not active (i.e., disabled or invalid) should be excluded from the results.
            List<AclDto> aclDtoList = (List<AclDto>)moduleIdAclMap.get(dto.getId());

            if (!CollectionUtils.isEmpty(aclDtoList)) {
                Collections.sort(aclDtoList, aclSeqComparator);
                dto.setAclList(aclDtoList);
            }

            // Recursion: It calls itself for all "Sub-Modules".
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }
    }


    /**
     * Build hierarchical ACL module tree.
     * The "Entry Point" for generating a nested, tree-like structure of permission modules.
     * It serves as the bridge between a flat database table and a hierarchical UI component.
     */
    public List<AclModuleLevelDto> aclModuleTree() {

        // Step 1. Fetch (Database)
        // Data Retrieval: Fetching the "Flat" List. The data is just a collection of independent rows.
        // Even though each module has a parentId, they aren't "connected" in memory yet.
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();

        // Step 2. Convert (Transformation)
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();

        for (SysAclModule aclModule : aclModuleList) {
            // SysAclModule doesn't have a List<AclModuleLevelDto> children field to hold sub-modules.
            // We need the DTO version to build the "branches" of the tree.
            // Iterate through every database entity and uses the adapt() method to create an AclModuleLevelDto.
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }
        // Step 3. Tree Construction
        return aclModuleListToTree(dtoList);
    }


    /**
     * The tree-building process: Converts flat ACL module list into hierarchical structure.
     * Its job is to take a flat pile of modules and organize them into a structured hierarchy
     * based on their level string (e.g., 0, 0.1, 0.1.2).
     * It uses a high-performance memory mapping strategy.
     */
    public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList) {

        // if there’s no data, return an empty list immediately to avoid NullPointerExceptions later.
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }

        // level -> [aclmodule1, aclmodule2, ...] Map<String, List<Object>>
        // A Multimap (from Google Guava) is like a Dictionary where one Key can have multiple Values.
        //Key: The level string (e.g., "0.1")
        //Value: A list of all modules belonging to that level.
        Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();

        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto dto : dtoList) {
            // Fill the Multimap for quick lookup later.
            levelAclModuleMap.put(dto.getLevel(), dto);

            // Identify the "Root" modules (top-level folders) to start the tree.
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // Sorting and Branching
        // It sorts the top-level modules based on their seq (sequence) number (e.g., so "System Management" appears before "User Settings").
        Collections.sort(rootList, aclModuleSeqComparator);
        // The recursive function that actually "plugs" the children into their parent's aclModuleList.
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);

        return rootList;
    }


    /**
     * Recursively builds ACL module tree structure.
     * The final "engine" builds the tree.
     * It uses Depth-First Recursion to stitch the parent modules and child modules together.
     */
    public void transformAclModuleTree(List<AclModuleLevelDto> dtoList,
                                       String level,
                                       Multimap<String, AclModuleLevelDto> levelAclModuleMap) {

        for (int i = 0; i < dtoList.size(); i++) {

            AclModuleLevelDto dto = dtoList.get(i);

            // Step 1. Calculate the "Target" Level
            // It looks at the current module and asks: "If I had children, what would their level string be?"
            // If the current module is ID: 5 and its level is "0.1", the nextLevel for its children will be "0.1.5".
            String nextLevel = LevelUtil.calculateLevel(level, dto.getId());

            // Step 2. Fetch and Link Children
            List<AclModuleLevelDto> tempList = (List<AclModuleLevelDto>) levelAclModuleMap.get(nextLevel);

            // If it finds any modules, it sorts them by their sequence (seq) and attaches them to the current module's aclModuleList.
            // This creates one "branch" of the tree.
            if (!CollectionUtils.isEmpty(tempList)) {
                Collections.sort(tempList, aclModuleSeqComparator);
                dto.setAclModuleList(tempList);
                // The Recursive Dive
                transformAclModuleTree(tempList, nextLevel, levelAclModuleMap);
            }
        }
    }



    /**
     * Builds department hierarchy tree.
     * 2016-02-16
     */
    public List<DeptLevelDto> deptTree() {

        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtoList = Lists.newArrayList();

        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }

        return deptListToTree(dtoList);
    }


    /**
     * Converts flat department list into hierarchical tree structure.
     * 2016-02-16
     */
    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {

        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }

        // level -> [dept1, dept2, ...] Map<String, List<Object>>
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();

        for (DeptLevelDto dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // Sort by sequence in ascending order
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });

        // Recursively generate tree
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);

        return rootList;
    }


    /**
     * Recursively builds department tree.
     * constructs a hierarchical tree of departments from a flat structure.
     * deptLevelList: A list of department DTOs (Data Transfer Objects) at the current level.
     * level: A string representing the current “level” in the hierarchy (e.g., "0", "0.1", "0.1.3").
     * levelDeptMap: A Multimap mapping levels to a list of departments at that level. This is pre-built before calling this method.
     * 2016-02-16
     */
    public void transformDeptTree(List<DeptLevelDto> deptLevelList,
                                  String level,
                                  Multimap<String, DeptLevelDto> levelDeptMap) {

        for (int i = 0; i < deptLevelList.size(); i++) {
            // Loop through all departments at the current hierarchy level.
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            // Calculate the level string for the next layer in the hierarchy.
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // Fetch the child departments of the current department from the levelDeptMap.
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);

            if (!CollectionUtils.isEmpty(tempDeptList)) {
                // Sort children by some sequence/order (deptSeqComparator).
                Collections.sort(tempDeptList, deptSeqComparator);
                // Set the children list to the current department (deptLevelDto.setDeptList).
                deptLevelDto.setDeptList(tempDeptList);
                // Recursively call transformDeptTree on the children to build the full hierarchy.
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }


    /**
     * Comparator for department ordering.
     */
    public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };


    /**
     * Comparator for ACL module ordering.
     */
    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };


    // Comparator for ACL permission ordering.
    public Comparator<AclDto> aclSeqComparator = Comparator.comparingInt(AclDto::getSeq);
}
