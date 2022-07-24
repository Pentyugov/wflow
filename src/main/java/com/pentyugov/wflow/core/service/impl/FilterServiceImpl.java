package com.pentyugov.wflow.core.service.impl;


import com.pentyugov.wflow.core.domain.entity.BaseEntity;
import com.pentyugov.wflow.core.service.FilterService;
import com.pentyugov.wflow.web.payload.request.FiltersRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import java.lang.reflect.Field;

@Service(FilterService.NAME)
public class FilterServiceImpl implements FilterService {


    @Override
    public String buildQueryString(Class<?> clazz, FiltersRequest filterRequest) {
        StringBuilder builder = new StringBuilder();

        Entity annotation = clazz.getAnnotation(Entity.class);
        if (annotation != null) {
            builder
                    .append("FROM ")
                    .append(annotation.name())
                    .append(" e WHERE e.id in :ids ");

            for (FiltersRequest.Filter filter : filterRequest.getFilters()) {
                if (StringUtils.hasText(filter.getProperty()) && StringUtils.hasText((filter.getCondition()))) {

                    Field field = getProperty(clazz, filter.getProperty());
                    if (field != null) {
                        boolean assignableFrom = BaseEntity.class.isAssignableFrom(field.getType());
                        if (assignableFrom) {
                            builder
                                    .append("and e.")
                                    .append(filter.getProperty())
                                    .append(".id in :")
                                    .append(filter.getProperty())
                                    .append(" ");
                        } else {
                            builder
                                    .append("and e.")
                                    .append(filter.getProperty())
                                    .append(" in :")
                                    .append(filter.getProperty())
                                    .append(" ");
                        }
                    }
                }
            }
        }

        return builder.toString();
    }

    private Field getProperty(Class<?> clazz, String property) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(property);
        } catch (NoSuchFieldException ignored) {
            if (clazz.getSuperclass() != null) {
                field = getProperty(clazz.getSuperclass(), property);
            }
        }

        return field;
    }
}
