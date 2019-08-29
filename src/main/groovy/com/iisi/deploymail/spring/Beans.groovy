package com.iisi.deploymail.spring

import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.tool.HtmlResource
import groovy.sql.Sql
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class Beans {

    @Bean
    HtmlResource htmlResource() {
        new HtmlResource('html', '.html')
    }

    @Bean
    FtlProvider ftlProvider() {
        File ftlTemplateDir = new ClassPathResource('templates').getFile()
        if (ftlTemplateDir == null || !ftlTemplateDir.exists()) {
            throw new RuntimeException("template directory not found in resources folder")
        }
        new FtlProvider(ftlTemplateDir)
    }

    @Bean
    Sql gSql() {
        def env = System.getenv()
        def url = env.get('pgsUrl') ?: 'jdbc:postgresql://localhost:5432/postgres?currentSchema=dbo'
        def user = env.get('pgsUser') ?: 'postgres'
        def password = env.get('pgsPwd') ?: 'p@ssw0rd'
        def dbConnParams = [
                url     : url,
                user    : user,
                password: password,
                driver  : 'org.postgresql.Driver'
        ]
        Sql.newInstance(dbConnParams)
    }
}
