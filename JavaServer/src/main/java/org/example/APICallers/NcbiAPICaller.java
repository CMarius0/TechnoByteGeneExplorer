package org.example.APICallers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NcbiAPICaller extends AbstractAPICaller{

    private static final String TOOL_PARAM = "&tool=GeneExplorer";
    private static final String EMAIL_PARAM = "&email=raulcostea434@yahoo.com";

    public NcbiAPICaller(String base_url) {
        super(base_url);
    }

    public Integer getGeneIdFromSymbol(String symbol) throws IOException {
        HttpURLConnection conn = getConnection( "esearch.fcgi?db=gene&term=" + symbol + "[gene]+AND+Homo+sapiens[orgn]&retmode=json");
        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        JSONObject jsonObject = new JSONObject(reader.readLine());
        return Integer.parseInt(jsonObject.getJSONObject("esearchresult").getJSONArray("idlist").get(0).toString());
    }

    public JSONObject getGeneInfoFromID(Integer id) throws Exception {
        HttpURLConnection conn = getConnection("esummary.fcgi?db=gene&id=" + id + "&retmode=json");

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
        test.put("summary",test.getString("summary").split("\\.")[0] + '.');
        int n = 10;
        var lista = getDiseasesByGeneId(id.toString());
        JSONArray array = new JSONArray();
        for (var x: lista){
            array.put(x.split("[,;]")[0].replaceAll(" SUSCEPTIBILITY [0-9]",""));
            n--;
            if (n==0)
                break;
        }
        test.put("diseases",array);
        return test;
    }

    public List<String> getDiseasesByGeneId(String geneId) throws Exception {
        List<String> diseaseNames = new ArrayList<>();

        JSONObject elinkResponse = getJsonFromUrl("elink.fcgi?dbfrom=gene&id=" + geneId + "&db=omim&tool=GeneExplorer&email=raulcostea434@yahoo.com&retmode=json");
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
        String esummaryUrl = "esummary.fcgi?db=omim&id=" + joinedIds + TOOL_PARAM + EMAIL_PARAM + "&retmode=json";
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

    private JSONObject getJsonFromUrl(String urlStr) throws Exception {
        HttpURLConnection con = getConnection(urlStr);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) content.append(line);
        return new JSONObject(content.toString());
    }
}
