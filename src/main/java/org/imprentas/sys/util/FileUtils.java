package org.imprentas.sys.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;

public class FileUtils {

    private static final Log log = LogFactory.getLog(FileUtils.class);

    public static String GET_CONTENT_DISPOSITION(String fileExtension) {
        String type = "attachment";
        if (fileExtension.contains("data:image") || fileExtension.contains("data:application/pdf")) {
            type = "inline";
        }
        return type;
    }

    public static void ADD_FILE_RESPONSE_HEADERS(HttpServletResponse response, String fileExt, String fileName) {

        try {
            String fileNameTrim = fileName.trim();
            String auxContentDisp = GET_CONTENT_DISPOSITION(fileExt);
            String contentDisposition = String.format("%s; filename=\"%s\"", auxContentDisp, fileNameTrim);
            int beginSubstr = 0;
            if (fileExt.startsWith("data:")) {
                beginSubstr = 5;
            }

            String contentTypePart = fileExt.substring(beginSubstr);
            String contentType = String.format("%s; name=\"%s\"", contentTypePart, fileNameTrim);

            response.setContentType(contentType);
            response.setHeader("Content-disposition", contentDisposition);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);

        } catch (Throwable ex) {
            log.error("Error al agregar cabeceras a respuesta", ex);
            System.out.println(String.format("Error al agregar cabeceras a respuesta: %s", ex.getMessage()));
            ex.printStackTrace();
        }


    }

}
