package challenge.users;

import challenge.FileFactrory.FileFactory;
import challenge.FileFactrory.JsonFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class User {

    static JSONArray userJsonArray, ticketJsonArray, organizationJsonArray;
    FileFactory fileFactory = new FileFactory();
    ArrayList<LinkedHashMap<String, String>> linkedHashMapArrayList = new ArrayList<>();

    public User() {
        JsonFile user = fileFactory.getFile("USER");
        JsonFile organization = fileFactory.getFile("ORGANIZATION");
        JsonFile ticket = fileFactory.getFile("TICKET");

        userJsonArray = user.getFile();
        organizationJsonArray = organization.getFile();
        ticketJsonArray = ticket.getFile();
    }

    public boolean validateSearchTerm(String term) {
        boolean isValidated = false;
        if (userJsonArray.getJSONObject(0).has(term)) {
            isValidated = true;
        }
        return isValidated;
    }

    public ArrayList<LinkedHashMap<String, String>> search(String term, String value) {
        boolean isAvailable = false;
        for (int i = 0; i < userJsonArray.length(); i++) {
            findByField(term, value, userJsonArray.getJSONObject(i));
        }
        return linkedHashMapArrayList;
    }

    private void findByField(String term, String value, JSONObject jsonObject) {
        try {

            switch (term) {
                case "_id":
                case "organization_id":
                    if (jsonObject.getInt(term) == Integer.parseInt(value)) {
                        getUser(jsonObject);
                    }
                    break;
                case "url":
                case "external_id":
                case "name":
                case "alias":
                case "created_at":
                case "locale":
                case "timezone":
                case "last_login_at":
                case "email":
                case "phone":
                case "signature":
                case "role":
                    if (jsonObject.getString(term).equalsIgnoreCase(value)) {
                        getUser(jsonObject);
                    }
                    break;
                case "active":
                case "verified":
                case "shared":
                case "suspended":
                    if (jsonObject.getBoolean(term) == Boolean.parseBoolean(value)) {
                        getUser(jsonObject);
                    }
                    break;
                case "tags":
                    for (int i = 0; i < jsonObject.getJSONArray(term).length(); i++) {
                        if (jsonObject.getJSONArray(term).getString(i).equalsIgnoreCase(value)) {
                            getUser(jsonObject);
                        }
                    }
                    break;
                case "quit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Searching users for " + term + " with a value of" + value + "\n No results found");

            }

        } catch (JSONException ex) {

        } catch (NumberFormatException ex) {
            System.out.println("Please Enter Valid input");
        }
    }

    private void getUser(JSONObject jsonObject) {
        LinkedHashMap<String, String> userDetailsMap = new LinkedHashMap<>();
        userDetailsMap.put("external_id", jsonObject.getString("external_id"));
        userDetailsMap.put("name", jsonObject.getString("name"));
        userDetailsMap.put("alias", jsonObject.getString("alias"));
        userDetailsMap.put("active", String.valueOf(jsonObject.getBoolean("active")));
        userDetailsMap.put("verified", String.valueOf(jsonObject.getBoolean("verified")));
        userDetailsMap.put("shared", String.valueOf(jsonObject.getBoolean("shared")));
        userDetailsMap.put("locale", jsonObject.getString("locale"));
        userDetailsMap.put("timezone", jsonObject.getString("timezone"));
        userDetailsMap.put("email", jsonObject.getString("email"));
        userDetailsMap.put("phone", jsonObject.getString("phone"));
        userDetailsMap.put("signature", jsonObject.getString("signature"));
        userDetailsMap.put("organization_id", String.valueOf(jsonObject.getInt("organization_id")));
        userDetailsMap.put("tags", jsonObject.getJSONArray("tags").toString());
        userDetailsMap.put("suspended", String.valueOf(jsonObject.getBoolean("suspended")));
        userDetailsMap.put("role", jsonObject.getString("role"));
        userDetailsMap.put("organization_name", getOrganizationName(jsonObject.getInt("organization_id")));
        ArrayList<String> tickets = getTickets(jsonObject.getInt("_id"));

        for (int i = 0; i < tickets.size(); i++) {
            userDetailsMap.put("ticket_" + i, tickets.get(i));
        }

        linkedHashMapArrayList.add(userDetailsMap);
    }

    private String getOrganizationName(int organization_id) {
        String organizationName = "";
        for (int i = 0; i < organizationJsonArray.length(); i++) {
            int id = organizationJsonArray.getJSONObject(i).getInt("_id");
            if (id == organization_id) {
                organizationName = organizationJsonArray.getJSONObject(i).getString("name");
                break;
            }
        }
        return organizationName;
    }

    public void printDetails(LinkedHashMap<String, String> userDetailsMap) {
        userDetailsMap.forEach((k, v) -> System.out.println(k + " \t\t\t\t\t\t\t " + v));
        System.out.println("\n");

    }

    private ArrayList getTickets(int id) {
        ArrayList<String> tickets = new ArrayList<>();
        for (int i = 0; i < ticketJsonArray.length(); i++) {
            int submitter_id = ticketJsonArray.getJSONObject(i).getInt("submitter_id");
            if (id == submitter_id) {
                tickets.add(ticketJsonArray.getJSONObject(i).getString("subject"));
            }
        }
        return tickets;
    }
    public ArrayList<String> getSearchableFields() {
        ArrayList<String> fieldsList = new ArrayList<>();
        JSONObject jsonObject = userJsonArray.getJSONObject(0);
        JSONArray key = jsonObject.names();
        for (int i = 0; i < key.length(); ++i) {
            fieldsList.add(key.getString(i));
        }
        return fieldsList;
    }

}
