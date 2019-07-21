package org.imprentas.sys.servlets;



import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class RestClient {

    //private static String REST_URI = "http://localhost:6543/rest/jobdoc";
    private static String REST_URI = "http://157.230.129.131:6543/rest/jobdoc";

    private Client client = ClientBuilder.newClient();


    public String createJsonEmployee(String emp_esquema,
                                       String jobid,
                                       String path) {

        String uri = String.format("%s?emp_esquema=%s&jobid=%s&path=%s", REST_URI,emp_esquema, jobid, path);

        //RestResponse response =  client.target(REST_URI).request(MediaType.TEXT_PLAIN).get(String.class);
        String response =  client.target(uri).request(MediaType.TEXT_PLAIN).get(String.class);

        return response;

    }

    public String getTempJrxml(String emp_esquema, Integer codigoRep) {
        String uri = String.format("%s?acc=template&emp_esquema=%s&codrep=%s", REST_URI,emp_esquema, codigoRep);

        //RestResponse response =  client.target(REST_URI).request(MediaType.TEXT_PLAIN).get(String.class);
        String response =  client.target(uri).request(MediaType.TEXT_PLAIN).get(String.class);

        return response;
    }

    public String saveOrUpdateDoc(String emp_esquema, Integer jobId, String pathFile) {
        String uri = String.format("%s?acc=save&emp_esquema=%s&jobId=%s&path=%s", REST_URI,emp_esquema, jobId, pathFile);

        String response =  client.target(uri).request(MediaType.TEXT_PLAIN).get(String.class);

        return response;

    }

    public String getPathSaveDoc(String emp_esquema) {
        String uri = String.format("%s?acc=pathSaveDoc&emp_esquema=%s", REST_URI,emp_esquema);
        System.out.println("path save doc uri es");
        System.out.println(uri);
        String response =  client.target(uri).request(MediaType.TEXT_PLAIN).get(String.class);
        return response;
    }

    public String getPathDocSaved(String emp_esquema, String jobid) {
        String uri = String.format("%s?acc=pathDocSaved&emp_esquema=%s&jobid=%s", REST_URI,emp_esquema,jobid);
        String response =  client.target(uri).request(MediaType.TEXT_PLAIN).get(String.class);
        return response;
    }


}
