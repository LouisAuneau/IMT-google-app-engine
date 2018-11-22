package com.zenika.zencontact.resource;

import com.zenika.zencontact.domain.Message;
import com.zenika.zencontact.domain.blob.PhotoService;
import com.google.appengine.api.blobstore.BlobKey;
import org.joda.time.DateTime;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(name = "PhotoResource", value = "/api/v0/photo/*")
public class PhotoResource extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(PhotoResource.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo(); // /{id}/{key}
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 0) {
            response.setStatus(404);
            return;
        }
        Long id = Long.valueOf(pathParts[1]);
        String blobkey = pathParts[2];
        PhotoService.getInstance().serve(new BlobKey(blobkey), response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo(); // /{id}
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 0) {
            response.setStatus(404);
            return;
        }
        LOG.log(Level.INFO, "pathParts " + pathParts);
        Long id = Long.valueOf(pathParts[1]);
        PhotoService.getInstance().updatePhoto(id, request);
        response.setContentType("text/plain");
        response.getWriter().println("ok");
    }

}