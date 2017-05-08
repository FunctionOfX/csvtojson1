import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.util.*;

/**
 * Created by Haiping on 05/05/2017.
 */


public class Main {



    public static void main(String[] args) {

        Map<String, String> mapcode = new HashMap<>();
        Map<String, String> mapdes = new HashMap<>();
        Map<String, Set<String[]>> patienttolabresult = new HashMap<>();
        Map<String, Set<String[]>> labresulttopanel = new HashMap<>();
        String codename = "labresults-codes.csv";
        File codes = new File (codename);
        try {
        Scanner newinputStream= new Scanner(codes);
            newinputStream.nextLine();
            while (newinputStream.hasNext()) {
            String data= newinputStream.nextLine();
            String[] values = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            mapcode.put(values[0],values[1]);
            mapdes.put(values[0],values[2]);
            }
            newinputStream.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }



        String filename = "labresults.csv";
        File file = new File(filename);

        try {
            Scanner inputStream= new Scanner(file);
            inputStream.nextLine();
            while (inputStream.hasNext()) {
                String data= inputStream.nextLine();
                String[] values= data.split(",",-1);
                if (patienttolabresult.containsKey(values[0])){
                    Set<String[]> temp = patienttolabresult.get(values[0]);
                    String[] array = new String[3];
                    array[0]=values[1];
                    array[1]=values[2];
                    array[2]=values[3];
                    temp.add(array);
                    patienttolabresult.put(values[0],temp);
                } else{
                    Set<String[]> temp = new HashSet<>();
                    String[] array = new String[3];
                    array[0]=values[1];
                    array[1]=values[2];
                    array[2]=values[3];
                    temp.add(array);
                    patienttolabresult.put(values[0],temp);
                }



                String[] temp1 = new String[32];
                for (int i = 0; i<32; i++){
                    temp1[i]=values[i+2];
                }
                if (labresulttopanel.containsKey(values[1])){
                    Set<String[]> temp = labresulttopanel.get(values[1]);


                    temp.add(temp1);
                    labresulttopanel.put(values[1],temp);
                } else{
                    Set<String[]> temp = new HashSet<>();
                    temp.add(temp1);
                    labresulttopanel.put(values[1],temp);
                }






            }
            inputStream.close();


        } catch (FileNotFoundException e){
            e.printStackTrace();
        }


        JSONObject patientsob = new JSONObject();



        for (String patient : patienttolabresult.keySet()) {
            JSONArray lab_resultsar = new JSONArray();
           for (String[] labresult: patienttolabresult.get(patient)){
               JSONObject lab_resultsob = new JSONObject();
               JSONObject profile = new JSONObject();
               JSONArray panelar = new JSONArray();

               for (String[] panel: labresulttopanel.get(labresult[0])){
                   JSONObject panelob = new JSONObject();


                   panelob.put("label", mapdes.get(panel[28]));

                   int i=5;
                   boolean stop=false;
                   while (i<28 && !stop){
                       String[] keyvalue = panel[i].split("~",-1);
                       if (keyvalue[0]==panel[28]) {
                           panelob.put("valve", keyvalue[1]);
                           panelob.put("label", keyvalue[2]);
                           stop =true;
                       }
                       i++;
                   }
                   panelob.put("unit", panel[29]);
                   panelob.put("lower", panel[30]);
                   panelob.put("upper", panel[31]);
                   panelar.put(panelob);
               }
               lab_resultsob.put("timestamp", labresult[0]);
               profile.put("name", labresult[1]);
               profile.put("code", labresult[2]);
               lab_resultsob.put("profile", profile);
               lab_resultsob.put("panel", panelar);
               lab_resultsar.put(lab_resultsob);
            }
            patientsob.put("id", patient);
            patientsob.put("firstname", "");
            patientsob.put("lastName", "");
            patientsob.put("dob","");
            patientsob.put("lab_results", lab_resultsar);
        }
        System.out.println(patientsob);





    }

}
