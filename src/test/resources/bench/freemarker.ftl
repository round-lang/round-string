<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperName}">
    <#list methodList as method>
        <#if "${method.sqlType}" =="select">
          <select id="${method.methodName}" resultType="${method.type}">
              ${method.desc}
          </select>
        <#elseif "${method.sqlType}" == "insert">
          <insert id="${method.methodName}" resultType="${method.type}">
              ${method.desc}
          </insert>
        </#if>
    </#list>
</mapper>