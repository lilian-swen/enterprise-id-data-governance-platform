package com.idgovern.service;

import com.google.common.base.Preconditions;
import com.idgovern.beans.PageQuery;
import com.idgovern.beans.PageResult;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysUserMapper;
import com.idgovern.exception.ParamException;
import com.idgovern.model.SysUser;
import com.idgovern.param.UserParam;
import com.idgovern.util.BeanValidator;
import com.idgovern.util.IpUtil;
import com.idgovern.util.MD5Util;
import com.idgovern.util.PasswordUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * System User Service.
 *
 * <p>
 * This service layer component handles business logic related to system users
 * within the RBAC (Role-Based Access Control) module.
 * </p>
 *
 * <p>
 * Responsibilities include:
 * <ul>
 *     <li>User creation and update</li>
 *     <li>Data validation and uniqueness checks</li>
 *     <li>Password encryption</li>
 *     <li>Pagination queries</li>
 *     <li>Audit logging</li>
 * </ul>
 * </p>
 *
 * <p>
 * All validation logic is performed before persistence operations.
 * Changes are recorded via {@link SysLogService} for auditing purposes.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-18 | Lilian S.| Initial creation                |
 * | 1.1     | 2016-03-03 | Updated  | Professional documentation added|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.1
 * @since 1.0
 */
@Service
public class SysUserService {

    /**
     * Data access layer for system users.
     */
    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * Log service used to record user modification history.
     */
    @Resource
    private SysLogService sysLogService;


    /**
     * Creates a new system user.
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Validate input parameters</li>
     *     <li>Check telephone and email uniqueness</li>
     *     <li>Generate and encrypt password</li>
     *     <li>Set audit fields (operator, IP, timestamp)</li>
     *     <li>Persist user record</li>
     *     <li>Save operation log</li>
     * </ol>
     * </p>
     *
     * @param param user creation parameters
     *              2016-02-18
     */
    public void save(UserParam param) {

        BeanValidator.check(param);

        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("Telephone number is already in use");
        }

        if(checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("Email address is already in use");
        }

        // Generate a random initial password
        String password = PasswordUtil.randomPassword();

        //TODO: replace temporary fixed password in production
        password = "12345678";

        // Encrypt password using MD5
        String encryptedPassword = MD5Util.encrypt(password);

        SysUser user = SysUser.builder()
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .password(encryptedPassword)
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        // Set audit information
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        // TODO: Send initial password to user via email
        sysUserMapper.insertSelective(user);
        // Record creation log
        sysLogService.saveUserLog(null, user);
    }


    /**
     * Updates an existing system user.
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Validate parameters</li>
     *     <li>Check uniqueness constraints</li>
     *     <li>Load existing user</li>
     *     <li>Update modified fields</li>
     *     <li>Persist changes</li>
     *     <li>Record audit log</li>
     * </ol>
     * </p>
     *
     * @param param user update parameters
     *              2016-02-18
     */
    public void update(UserParam param) {

        BeanValidator.check(param);

        if(checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("Telephone number is already in use");
        }

        if(checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("Email address is already in use");
        }

        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());

        Preconditions.checkNotNull(before, "The user to be updated does not exist");

        SysUser after = SysUser.builder()
                .id(param.getId())
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        // Set audit information
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysUserMapper.updateByPrimaryKeySelective(after);

        // Record update log (before and after comparison)
        sysLogService.saveUserLog(before, after);
    }


    /**
     * Checks whether an email already exists in the system.
     *
     * @param mail   email address
     * @param userId user ID (excluded when updating)
     * @return true if exists, false otherwise
     */
    public boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    /**
     * Checks whether a telephone number already exists in the system.
     *
     * @param telephone telephone number
     * @param userId    user ID (excluded when updating)
     * @return true if exists, false otherwise
     */
    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    /**
     * Finds a user by keyword.
     *
     * <p>
     * The keyword may represent username, email, or telephone.
     * </p>
     *
     * @param keyword search keyword
     * @return SysUser entity
     */
    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }


    /**
     * Retrieves paginated users by department ID.
     *
     * @param deptId department ID
     * @param page   pagination parameters
     * @return paginated result
     */
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {

        BeanValidator.check(page);

        int count = sysUserMapper.countByDeptId(deptId);

        if (count > 0) {

            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
            return PageResult.<SysUser>builder().total(count).data(list).build();

        }

        return PageResult.<SysUser>builder().build();
    }

    /**
     * Retrieves all users in the system.
     *
     * @return list of users
     */
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }
}
