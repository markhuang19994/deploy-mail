package com.iisi.deploymail

import com.iisi.deploymail.job.BgImageUpdateJob
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DeployMailApplication {

    static void main(String[] args) {
        def ctx = SpringApplication.run(DeployMailApplication, args)
        def t = new Thread({
            def job = ctx.getBean(BgImageUpdateJob.class)
            job.updateBgImage()
        })
        t.setDaemon(true)
        t.start()
    }

}
