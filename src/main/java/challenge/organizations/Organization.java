package challenge.organizations;

import challenge.FileFactrory.FileFactory;
import challenge.FileFactrory.JsonFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Organization {
    static JSONArray userJsonArray, ticketJsonArray, organizationJsonArray;
    FileFactory fileFactory = new FileFactory();
    ArrayList<LinkedHashMap<String, String>> linkedHashMapArrayList = new ArrayList<>();

    public Organization() {
        JsonFile user = fileFactory.getFile("USER");
        JsonFile organization = fileFactory.getFile("ORGANIZATION");
        JsonFile ticket = fileFactory.getFile("TICKET");

        userJsonArray = user.getFile();
        organizationJsonArray = organization.getFile();
        ticketJsonArray = ticket.getFile();
    }

    public boolean validateSearchTerm(String term) {
        boolean isValidated = false;
        if (organizationJsonArray.getJSONObject(0).has(term)) {
            isValidated = true;
        }
        return isValidated;
    }

    public ArrayList<LinkedHashMap<String, String>> search(String term, String value) {

        for (int i = 0; i < organizationJsonArray.length(); i++) {
            findByField(term, value, organizationJsonArray.getJSONObject(i));
        }
        return linkedHashMapArrayList;
    }

    private LinkedHashMap<String, String> findByField(String term, String value, JSONObject jsonObject) {
        try {
            switch (term) {
                case "_id":
                    if (jsonObject.getInt(term) == Integer.parseInt(value)) {
                        return getOrganization(jsonObject);
                    }
                    break;
                case "url":
                case "external_id":
                case "name":
                case "created_at":
                case "details":
                    if (jsonObject.getString(term).equalsIgnoreCase(value)) {
                        return getOrganization(jsonObject);
                    }
                    break;
                case "tags":
                case "domain_names":
                    for (int i = 0; i < jsonObject.getJSONArray(term).length(); i++) {
                        if (jsonObject.getJSONArray(term).getString(i).equalsIgnoreCase(value)) {
                            return getOrganization(jsonObject);
                        }
                    }
                    break;
                case "shared_tickets":
                    if (jsonObject.getBoolean(term) == Boolean.parseBoolean(value)) {
                        return getOrganization(jsonObject);
                    }
                    break;
                default:
                    return null;
            }
        } catch (JSONException ex) {
            System.out.println(ex);
        } catch (NumberFormatException ex) {
            System.out.println("Please Enter Valid input");
            return null;
        }

        return null;
    }

    private LinkedHashMap<String, String> getOrganization(JSONObject jsonObject) {
        LinkedHashMap<String, String> organizationDetailsMap = new LinkedHashMap<>();
        organizationDetailsMap.put("_id", String.valueOf(jsonObject.getInt("_id")));
        organizationDetailsMap.put("url", jsonObject.getString("url"));
        organizationDetailsMap.put("external_id", jsonObject.getString("external_id"));
        organizationDetailsMap.put("name", jsonObject.getString("name"));
        organizationDetailsMap.put("created_at", jsonObject.getString("created_at"));
        organizationDetailsMap.put("details", jsonObject.getString("details"));
        organizationDetailsMap.put("tags", jsonObject.getJSONArray("tags").toString());
        organizationDetailsMap.put("domain_names", jsonObject.getJSONArray("domain_names").toString());
        organizationDetailsMap.put("shared_tickets", String.valueOf(jsonObject.getBoolean("shared_tickets")));

        ArrayList<String> usersNames = getUsersNames(jsonObject.getInt("_id"));
        for (int i = 0; i < usersNames.size(); i++) {
            organizationDetailsMap.put("user_" + i, usersNames.get(i));
        }
        ArrayList<String> tickets = getTickets(jsonObject.getInt("_id"));
        for (int i = 0; i < tickets.size(); i++) {
            organizationDetailsMap.put("tickets_" + i, tickets.get(i));
        }
        linkedHashMapArrayList.add(organizationDetailsMap);
        return organizationDetailsMap;
    }

    private ArrayList<String> getUsersNames(int id) {

        ArrayList<String> users = new ArrayList<>();
        for (int i = 0; i < userJsonArray.length(); i++) {
            int organizationId = userJsonArray.getJSONObject(i).optInt("organization_id");
            if (id == organizationId) {
                users.add(userJsonArray.getJSONObject(i).getString("name"));
            }
        }
        return users;
    }

    private ArrayList<String> getTickets(int id) {

        ArrayList<String> users = new ArrayList<>();
        for (int i = 0; i < ticketJsonArray.length(); i++) {
            int organizationId = ticketJsonArray.getJSONObject(i).optInt("organization_id");
            if (id == organizationId) {
                users.add(ticketJsonArray.getJSONObject(i).getString("subject"));
            }
        }

        return users;
    }
    public ArrayList<String> getSearchableFields() {
        ArrayList<String> fieldsList = new ArrayList<>();
        JSONObject jsonObject = organizationJsonArray.getJSONObject(0);
        JSONArray key = jsonObject.names();
        for (int i = 0; i < key.length(); ++i) {
            fieldsList.add(key.getString(i));
        }
        return fieldsList;
    }

}

