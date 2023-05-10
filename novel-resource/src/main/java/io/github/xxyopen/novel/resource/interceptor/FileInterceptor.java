package io.github.xxyopen.novel.resource.interceptor;

import com.esotericsoftware.kryo.io.Output;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class FileInterceptor implements HandlerInterceptor {
    @Value("${novel.file.upload.path}")
    private String fileUploadPath;

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestUri = request.getRequestURI();

        response.setDateHeader("expires", System.currentTimeMillis() + 60 * 60 * 24 * 10 * 1000);
        try (OutputStream out = response.getOutputStream(); InputStream input = new FileInputStream(fileUploadPath + requestUri)) {
            byte[] b = new byte[4096];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
        }
        return false;
    }
}
