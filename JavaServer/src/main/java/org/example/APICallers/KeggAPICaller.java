package org.example.APICallers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class KeggAPICaller extends AbstractAPICaller{
    public KeggAPICaller(String base_url) {
        super(base_url);
    }

    public ArrayList<String> getPathwaysFromID(Integer id) {
        try {
            HttpURLConnection conn = getConnection("link/pathway/hsa:" + id);

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

    public ArrayList<String> getDrugs() {
        try {
            HttpURLConnection conn = getConnection("list/drug");

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

    public List<String> extractGenesFromPathway(String pathwayId) throws Exception {
        List<String> geneSymbols = new ArrayList<>();

        HttpURLConnection con = getConnection("get/" + pathwayId + "/kgml");

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

    public String extractNtseqFromKeggGene(String geneKeggId) throws Exception {
        HttpURLConnection con = getConnection("get/" + geneKeggId);

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

    public List<String> extractDrugTarget(String geneId) throws Exception {
        HttpURLConnection con = getConnection("get/hsa:" + geneId);
        ArrayList<String> drugs = new ArrayList<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        boolean inSeq = false;

        while ((line = in.readLine()) != null) {
            if (line.startsWith("DRUG_TARGET")) {
                inSeq = true;
            }
            if (inSeq) {
                if (line.startsWith("BRITE")) break;
                drugs.add(line.trim().replaceAll("DRUG_TARGET ",""));
            }
        }
        in.close();
        return !drugs.isEmpty() ? drugs : null;
    }
}
