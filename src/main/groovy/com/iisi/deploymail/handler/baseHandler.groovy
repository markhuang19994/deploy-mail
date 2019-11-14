package com.iisi.deploymail.handler

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.service.BgImageService
import com.iisi.deploymail.service.DeployMailUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.nio.file.Files

@Controller
@RequestMapping(['/base'])
class baseHandler {

    @Autowired
    DeployMailUserService deployMailUserService

    @Autowired
    private BgImageService bgImageService

    @ResponseBody
    @GetMapping('/invalidateSession')
    String invalidateSession(HttpServletRequest request) {
        def session = request.getSession(false)
        if (session != null) {
            session.invalidate()
        }
        'success'
    }

    @ResponseBody
    @PostMapping('/getUserData')
    ResponseEntity<?> getUserData(HttpServletRequest request) {
        def session = request.getSession(false)
        def userName = String.valueOf(session.getAttribute(Constants.USER_ENG_NAME))
        def deployMailUser = deployMailUserService.getDeployMailUserByEngName(userName)
        ResponseEntity.ok(deployMailUser)
    }

    @ResponseBody
    @GetMapping('/background/image')
    ResponseEntity<?> getBackgroundImage(HttpServletResponse resp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.set('Cache-Control', 'max-age=300, public');
        def imgDir = bgImageService.getBackgroundImageDir()
        if (imgDir.exists()) {
            def images = imgDir.listFiles()
            if (images != null && images.length > 1) {
                int hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                int idx = ((int) (hr / 12))
                return new ResponseEntity<Object>(Files.readAllBytes(images[idx].toPath()), headers, HttpStatus.ACCEPTED)
            }
        }

        return new ResponseEntity<Object>(Files.readAllBytes(bgImageService.getDefaultBackgroundImage().toPath()), headers, HttpStatus.ACCEPTED)
    }

}
