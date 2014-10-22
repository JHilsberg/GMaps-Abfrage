package de.hszg.julian.gm_abfrage;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Julian on 22.10.2014.
 */
public class GetGMData extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... input) {
        int count = input.length;
        String coordinateData = null;

        for (int i = 0; i < count; i++) {
            String address = input[i];
            HttpResponse response = getXMLResponse(address);
            HttpEntity entity = response.getEntity();
            try {
                String data = EntityUtils.toString(entity);
                coordinateData = getCoordinates(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return coordinateData;
    }

    public HttpResponse getXMLResponse(String address) {
        String PROTOCOL = "http";
        String HOST = "maps.google.com";
        String PATH = "/maps/api/geocode/";
        String OUTPUT = "xml";
        String PARAMETERS = "?sensor=false&address=";
        String url = PROTOCOL + "://" + HOST + PATH + OUTPUT + PARAMETERS;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url + URLEncoder.encode(address, "UTF-8"));
            HttpResponse response = client.execute(request);
            return response;
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error", "Connection Error", e);
        }
        return null;
    }

    public String getCoordinates(String inputGMData) throws Exception {
        InputSource source = new InputSource(new StringReader(inputGMData));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(source);

        XPath xpath = XPathFactory.newInstance().newXPath();

        String status = (String) xpath.evaluate("//status/text()", doc, XPathConstants.STRING);

        if (!status.equals("OK")) {
            return "Response not OK";
        } else {
            String lat = (String) xpath.evaluate("//geometry/location/lat/text()", doc,
                    XPathConstants.STRING);

            String lng = (String) xpath.evaluate("//geometry/location/lng/text()", doc,
                    XPathConstants.STRING);

            String formattedAddress = (String) xpath.evaluate("//result/formatted_address/text()", doc,
                    XPathConstants.STRING);

            return String.format("Latitude: %s\nLongitude: %s\nAddress: %s", lat, lng, formattedAddress);
        }
    }
}
