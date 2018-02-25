package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and other whitespace
        have been added for clarity).  Note the curly braces, square brackets, and double-quotes!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JSONObject jsonObject = new JSONObject();
       
            JSONArray rowHeaders = new JSONArray();
            JSONArray colHeaders = new JSONArray();
            JSONArray allData = new JSONArray(); // main array containing smaller arrays
            JSONArray subData; //indiviual arrays containing data entries 
            String[] record; //for processing individual lines

            record = full.get(0); //snag the first row for the colHeaders
            
            for (String str : record) { // add them to colHeaders
                colHeaders.add(str);
            }
           
            for(int i = 1; i < full.size(); i++){ //skip the first row
                record = full.get(i); 
                rowHeaders.add(record[0]); // add the first entry to every row to the rowHeaders
                subData = new JSONArray();
                
                for(int j = 1; j < record.length; j++){
                    subData.add(Integer.parseInt(record[j])); //suvData arrrays to be stored in allData array
                }
                
                allData.add(subData); // nest the arrays into big array
            }
            
            jsonObject.put("colHeaders", colHeaders);
            jsonObject.put("rowHeaders", rowHeaders);
            jsonObject.put("data", allData);
            
            results = JSONValue.toJSONString(jsonObject); 

        }
		
		
        
        catch(IOException e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            JSONArray colHeaders = (JSONArray) jsonObject.get("colHeaders");
            JSONArray rowHeaders = (JSONArray) jsonObject.get("rowHeaders");
            JSONArray data = (JSONArray) jsonObject.get("data");
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            
            for(int i = 0; i < colHeaders.size(); i++ ){ //get the column headers
                if(i == colHeaders.size() - 1)
                    writer.append("\"" + colHeaders.get(i) + "\"");
                else
                    writer.append("\"" + colHeaders.get(i) + "\",");
            }
            
            writer.append("\n");
            
            for(int i = 0; i < rowHeaders.size(); i++){
                writer.append("\"" + rowHeaders.get(i) + "\",");
                JSONArray subdata = (JSONArray)data.get(i);
                for(int j = 0; j < subdata.size(); j++){
                    if(j == subdata.size() - 1)
                        writer.append("\"" + subdata.get(j) + "\"");
                    else
                        writer.append("\"" + subdata.get(j) + "\",");
                }
                writer.append("\n");
            }
            
            results += writer.toString();
            
        }
        
        catch(ParseException e) { return e.toString(); }
        
        return results.trim();
        
    }
	
}