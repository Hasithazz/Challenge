package challenge.Tickets;

import challenge.FileFactrory.FileFactory;
import challenge.FileFactrory.JsonFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Ticket {
    static JSONArray userJsonArray, ticketJsonArray, organizationJsonArray;
    FileFactory fileFactory = new FileFactory();
    ArrayList<LinkedHashMap<String, String>> linkedHashMapArrayList = new ArrayList<>();

    public Ticket() {
        JsonFile user = fileFactory.getFile("USER");
        JsonFile organization = fileFactory.getFile("ORGANIZATION");
        JsonFile ticket = fileFactory.getFile("TICKET");

        userJsonArray = user.getFile();
        organizationJsonArray = organization.getFile();
        ticketJsonArray = ticket.getFile();
    }

    public boolean validateSearchTerm(String term) {
        boolean isValidated = false;
        if (ticketJsonArray.getJSONObject(0).has(term)) {
            isValidated = true;
        }
        return isValidated;
    }

    public ArrayList<LinkedHashMap<String, String>> search(String term, String value) {
        for (int i = 0; i < ticketJsonArray.length(); i++) {
            findByField(term, value, ticketJsonArray.getJSONObject(i));
        }
        return linkedHashMapArrayList;
    }

    private void findByField(String term, String value, JSONObject jsonObject) {
        try {
            switch (term) {
                case "_id":
                case "url":
                case "external_id":
                case "created_at":
                case "type":
                case "subject":
                case "description":
                case "priority":
                case "status":
                case "due_at":
                case "via":
                    if (jsonObject.getString(term).equalsIgnoreCase(value)) {
                        getTicket(jsonObject);
                    }
                    break;
                case "submitter_id":
                case "assignee_id":
                case "organization_id":
                    if (jsonObject.getInt(term) == Integer.parseInt(value)) {
                        getTicket(jsonObject);
                    }
                    break;
                case "tags":
                    for (int i = 0; i < jsonObject.getJSONArray(term).length(); i++) {
                        if (jsonObject.getJSONArray(term).getString(i).equalsIgnoreCase(value)) {
                            getTicket(jsonObject);
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException ex) {
        } catch (NumberFormatException ex) {
            System.out.println("Please Enter Valid input");
        }

    }

    private void getTicket(JSONObject jsonObject) {
        LinkedHashMap<String, String> ticketDetailMap = new LinkedHashMap<>();
        ticketDetailMap.put("_id", jsonObject.getString("_id"));
        ticketDetailMap.put("url", jsonObject.getString("url"));
        ticketDetailMap.put("external_id", jsonObject.getString("external_id"));
        ticketDetailMap.put("created_at", jsonObject.getString("created_at"));
        ticketDetailMap.put("type", jsonObject.getString("type"));
        ticketDetailMap.put("subject", jsonObject.getString("subject"));
        ticketDetailMap.put("description", jsonObject.getString("description"));
        ticketDetailMap.put("priority", jsonObject.getString("priority"));
        ticketDetailMap.put("status", jsonObject.getString("status"));
        ticketDetailMap.put("submitter_id", String.valueOf(jsonObject.getInt("submitter_id")));
        ticketDetailMap.put("assignee_id", String.valueOf(jsonObject.getInt("assignee_id")));
        ticketDetailMap.put("organization_id", String.valueOf(jsonObject.getInt("organization_id")));
        ticketDetailMap.put("tags", jsonObject.getJSONArray("tags").toString());
        ticketDetailMap.put("has_incidents", String.valueOf(jsonObject.getBoolean("has_incidents")));
        ticketDetailMap.put("due_at", jsonObject.getString("due_at"));
        ticketDetailMap.put("via", jsonObject.getString("via"));
        ticketDetailMap.put("assignee_name", getUserName(jsonObject.getInt("assignee_id")));
        ticketDetailMap.put("submitter_name", getUserName(jsonObject.getInt("submitter_id")));
        ticketDetailMap.put("organization_name", getOrganizationName(jsonObject.getInt("organization_id")));

        linkedHashMapArrayList.add(ticketDetailMap);
    }

    private String getOrganizationName(int organization_id) {
        for (int i = 0; i < organizationJsonArray.length(); i++) {
            if (organization_id == organizationJsonArray.getJSONObject(i).optInt("_id")) {
                return organizationJsonArray.getJSONObject(i).optString("name");
            }
        }
        return null;
    }

    private String getUserName(int id) {
        for (int i = 0; i < userJsonArray.length(); i++) {
            if (id == userJsonArray.getJSONObject(i).optInt("_id")) {
                return userJsonArray.getJSONObject(i).optString("name");
            }
        }
        return null;
    }

    public ArrayList<String> getSearchableFields() {
        ArrayList<String> fieldsList = new ArrayList<>();
        JSONObject jsonObject = ticketJsonArray.getJSONObject(0);
        JSONArray key = jsonObject.names();
        for (int i = 0; i < key.length(); ++i) {
            fieldsList.add(key.getString(i));
        }
        return fieldsList;
    }
}
