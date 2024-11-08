package graph.db.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PnRc {

    static List<Map<String, String>> normRis(String file) {
		// TODO Auto-generated method stub
		try {
			List<Map<String, String>> recsToRt = new ArrayList<>();
			Map<String, String> map = new HashMap<>();
            
            InputStream is = ClassLoader.getSystemResourceAsStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = br.readLine();
            line = br.readLine();
			while(!line.startsWith("EF")){
				if(line.trim().isEmpty()){
					line = br.readLine();
					continue;
				}else{
					String idf = line.substring(0, 2).trim();
					if (!idf.trim().isEmpty()){
						switch (idf){
						case "FN":
						case "VR":
							line = br.readLine();
							break;
						case "ER":
							map.put("AU", map.get("AU").trim() + "-AAUU");
							recsToRt.add(map);
							line = br.readLine();
							map = new HashMap<>();
							break;
						default:
							map.put(idf, line.substring(3).trim().toString());
							line = br.readLine();
							while(line.startsWith("  ")){
								switch (idf){
								case "AU":
									map.put(idf, map.get(idf) + "-AAUU;" + line.trim());
									line = br.readLine();
									break;
								case "AF":
								case "C1":
								case "CR":
									map.put(idf, map.get(idf) + ";" + line.trim());
									line = br.readLine();
									break;
								default:
									map.put(idf, map.get(idf) + " " + line.trim());
									line = br.readLine();
									break;
								}}}}}}
			br.close();
			return recsToRt;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    public static String exportMerge(String file) {
        StringBuilder mergeQuery = new StringBuilder();
        List<Map<String, String>> pps = normRis(file);
        for (Map<String, String> pp : pps)
        {
            mergeQuery.append("MERGE (p:Paper {");

            String[] afs = pp.get("AF").split(";");
            String[] aus = pp.get("AU").split(";");
            for (int i = 0; i < afs.length; i++){
                if (i == 0) mergeQuery.append("AUTHORS:[\"" + aus[i].replace("-AAUU", "")+"("+afs[i]+")\"");
                else mergeQuery.append(",\""+aus[i].replace("-AAUU", "")+"("+afs[i]+")\"");
            }
            mergeQuery.append("]");
            for (String key : pp.keySet()) {
                mergeQuery.append(", " + key + ":\"" + pp.get(key) + "\"");
            }
            mergeQuery.append("})");
        }
        return mergeQuery.toString();
    }

    public static void main(String[] args) {
        String fileName = "savedrecs.txt";
        System.out.print(exportMerge(fileName));
    }
}
