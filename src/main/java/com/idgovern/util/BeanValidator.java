package com.idgovern.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.idgovern.exception.ParamException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for validating Java beans using JSR-303/JSR-380 annotations.
 *
 * <p>
 * Provides methods to validate single objects, collections, or multiple objects,
 * and throws a {@link ParamException} if validation fails. This is commonly used
 * in RBAC modules and service layer input validation.
 * </p>
 *
 * <p>
 * Features:
 * <ul>
 *     <li>Validate a single object and return a map of errors</li>
 *     <li>Validate a list of objects</li>
 *     <li>Validate multiple objects in a single call</li>
 *     <li>Directly throw {@link ParamException} if validation fails</li>
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
public class BeanValidator {

    /**
     * ValidatorFactory instance used to create Validator objects.
     */
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * Validates a single object based on JSR-303 annotations.
     *
     * @param t      the object to validate
     * @param groups optional validation groups
     * @param <T>    type of the object
     * @return a map of property paths and error messages, empty if no errors
     */
    public static <T> Map<String, String> validate(T t, Class... groups) {

        Validator validator = validatorFactory.getValidator();
        // Set validateResult = validator.validate(t, groups);
        Set<ConstraintViolation<T>> validateResult = validator.validate(t, groups);

        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            // LinkedHashMap errors = Maps.newLinkedHashMap();
            LinkedHashMap<String, String> errors = Maps.newLinkedHashMap();
            // Iterator iterator = validateResult.iterator();
            Iterator<ConstraintViolation<T>> iterator = validateResult.iterator();

            while (iterator.hasNext()) {
                // ConstraintViolation violation = (ConstraintViolation)iterator.next();
                ConstraintViolation<T> violation = iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    /**
     * Validates a collection of objects.
     *
     * <p>
     * Iterates through the collection and returns the first set of validation errors
     * found. If all objects are valid, returns an empty map.
     * </p>
     *
     * @param collection collection of objects to validate
     * @return a map of errors, empty if all objects are valid
     */
    public static Map<String, String> validateList(Collection<?> collection) {

        Preconditions.checkNotNull(collection);
        Iterator<?> iterator = collection.iterator();
        // Map errors;
        Map<String, String> errors;

        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }

            Object object = iterator.next();
            errors = validate(object, new Class[0]);

        } while (errors.isEmpty());

        return errors;
    }


    /**
     * Validates one or more objects.
     *
     * <p>
     * Useful for validating multiple parameters in a single service method call.
     * </p>
     *
     * @param first   the first object to validate
     * @param objects additional objects to validate
     * @return a map of errors, empty if all objects are valid
     */
    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }


    /**
     * Validates an object and directly throws a {@link ParamException} if any
     * validation errors are found.
     *
     * <p>
     * This is the preferred method for service-layer validation to enforce
     * business rules and reject invalid inputs immediately.
     * </p>
     *
     * @param param the object to validate
     * @throws ParamException if validation errors are found
     */
    public static void check(Object param) throws ParamException {

        Map<String, String> map = BeanValidator.validateObject(param);

        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }

    }
}
