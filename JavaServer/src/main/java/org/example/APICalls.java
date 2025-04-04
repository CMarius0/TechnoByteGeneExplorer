package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class APICalls {
    private static final String BASE_URL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
    private static final String TOOL_PARAM = "&tool=GeneExplorer";
    private static final String EMAIL_PARAM = "&email=raulcostea434@yahoo.com";


    private HttpURLConnection getConnection(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.connect();
        return connection;
    }

    public static Integer getGeneIdFromSymbol(String symbol) {
        try {
            APICalls api = new APICalls();
            HttpURLConnection conn = api.getConnection("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gene&term=" + symbol + "[gene]+AND+Homo+sapiens[orgn]&retmode=json");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            JSONObject jsonObject = new JSONObject(reader.readLine());
            return Integer.parseInt(jsonObject.getJSONObject("esearchresult").getJSONArray("idlist").get(0).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static JSONObject getGeneInfoFromID(Integer id) {
        try {
            APICalls api = new APICalls();
            HttpURLConnection conn = api.getConnection("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=gene&id=" + id + "&retmode=json");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            JSONObject jsonObject = new JSONObject(reader.readLine());
            JSONObject test = jsonObject.getJSONObject("result").getJSONObject(id.toString());

            Set<String> keysToKeep = Set.of("name", "summary","description");

            Set<String> keysToRemove = new HashSet<>();

            for (String key : test.keySet()) {
                if (!keysToKeep.contains(key)) {
                    keysToRemove.add(key);
                }
            }

            for (String key : keysToRemove) {
                test.remove(key);
            }
            test.put("summary",test.getString("summary").split("\\.")[0]);
            int n = 10;
            var lista = getDiseasesByGeneId(id.toString());
            JSONArray array = new JSONArray();
            for (var x: lista){
                array.put(x);
                n--;
                if (n==0)
                    break;
            }
            test.put("diseases",array);
            return test;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static List<String> getDiseasesByGeneId(String geneId) throws Exception {
        List<String> diseaseNames = new ArrayList<>();

        String elinkUrl = BASE_URL + "elink.fcgi?dbfrom=gene&id=" + geneId + "&db=omim" + TOOL_PARAM + EMAIL_PARAM + "&retmode=json";
        JSONObject elinkResponse = getJsonFromUrl(elinkUrl);
        JSONArray linkSets = elinkResponse.getJSONArray("linksets");

        if (linkSets.isEmpty()) return diseaseNames;

        JSONArray linkIds = linkSets.getJSONObject(0)
                .getJSONArray("linksetdbs")
                .getJSONObject(0)
                .getJSONArray("links");

        if (linkIds.isEmpty()) return diseaseNames;

        List<String> omimIds = new ArrayList<>();
        for (int i = 0; i < linkIds.length(); i++) {
            omimIds.add(linkIds.getString(i));
        }

        String joinedIds = String.join(",", omimIds);
        String esummaryUrl = BASE_URL + "esummary.fcgi?db=omim&id=" + joinedIds + TOOL_PARAM + EMAIL_PARAM + "&retmode=json";
        JSONObject omimSummary = getJsonFromUrl(esummaryUrl);
        JSONObject result = omimSummary.getJSONObject("result");

        for (String omimId : omimIds) {
            if (!result.has(omimId)) continue;
            JSONObject omim = result.getJSONObject(omimId);
            String title = omim.optString("title", "Unknown OMIM Entry");
            diseaseNames.add(title);
        }

        return diseaseNames;
    }

    private static JSONObject getJsonFromUrl(String urlStr) throws Exception {
        APICalls api = new APICalls();
        HttpURLConnection con = api.getConnection(urlStr);

        int retries = 3;
        while (retries-- > 0) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) content.append(line);
                return new JSONObject(content.toString());
            } catch (IOException e) {
                if (con.getResponseCode() == 429) {
                    System.out.println("Rate limit hit, retrying...");
                    Thread.sleep(1000);
                } else {
                    throw e;
                }
            }
        }
        throw new IOException("Failed after retries.");
    }

    public static ArrayList<String> getPathwaysFromID(Integer id) {
        try {
            APICalls api = new APICalls();
            HttpURLConnection conn = api.getConnection("https://rest.kegg.jp/link/pathway/hsa:" + id);

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> response = new ArrayList<>();
            String inputLine;
            int n = 100;
            while ((inputLine = reader.readLine()) != null & n!=0)
            {
                response.add(inputLine.split("\\s+")[1].replaceAll("path:",""));
                n--;
            }

            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<String> getDrugs() {
        try {
            APICalls api = new APICalls();
            HttpURLConnection conn = api.getConnection("https://rest.kegg.jp/list/drug");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> response = new ArrayList<>();
            String inputLine;
            int n = 100;
            while ((inputLine = reader.readLine()) != null & n!=0)
            {
                System.out.println(inputLine);
                n--;
            }

            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static List<String> extractGenesFromPathway(String pathwayId) throws Exception {
        List<String> geneSymbols = new ArrayList<>();

        APICalls api = new APICalls();
        HttpURLConnection con = api.getConnection("https://rest.kegg.jp/get/" + pathwayId + "/kgml");

        InputStream stream = con.getInputStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(stream);
        doc.getDocumentElement().normalize();

        NodeList entries = doc.getElementsByTagName("entry");
        for (int i = 0; i < entries.getLength(); i++) {
            Element entry = (Element) entries.item(i);
            if (!"gene".equals(entry.getAttribute("type"))) continue;

            NodeList graphicsList = entry.getElementsByTagName("graphics");
            if (graphicsList.getLength() > 0) {
                Element graphics = (Element) graphicsList.item(0);
                String geneName = graphics.getAttribute("name");
                if (!geneName.isEmpty()) geneSymbols.add(geneName);
            }
        }

        return geneSymbols;
    }

    public static String extractNtseqFromKeggGene(String geneKeggId) throws Exception {
        APICalls api = new APICalls();
        HttpURLConnection con = api.getConnection("https://rest.kegg.jp/get/" + geneKeggId);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        boolean inSeq = false;
        StringBuilder seq = new StringBuilder();

        while ((line = in.readLine()) != null) {
            if (line.startsWith("NTSEQ")) {
                inSeq = true;
                continue;
            }
            if (inSeq) {
                if (line.startsWith("///")) break;
                seq.append(line.trim());
            }
        }
        in.close();
        return !seq.isEmpty() ? seq.toString() : null;
    }

}