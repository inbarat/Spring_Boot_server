package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;



public class BL {
    // Store users by their ID
    private static Map<String, User> userMap = new HashMap<>();
    /**
     * Adds users from a given JSON string.
     * Validates each user by checking their ID and phone number.
     * returns a response map with status and invalid users list.
     *
     * @param json JSON string containing user data.
     * @return Map with response message and list of invalid users (if any).
     */
    public Map<String, Object> AddUserFromJson(String json){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        List<String> invalidUsers = new ArrayList<>();

        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Error processing JSON: " + e.getMessage());
            return Map.of("message", "Error processing JSON", "error", e.getMessage());
        }
        JsonNode usersArray = rootNode.get("users");
        String id;
        String name;
        String phoneNumber;
        String address;
        boolean isValidId;
        boolean isValidPhone;

        for (JsonNode user : usersArray) {
            id = user.get("id").asText();
            name = user.get("name").asText();
            phoneNumber = user.get("phoneNumber").asText();
            address = user.get("address").asText();

            isValidId = validateIsraeliId(id);
            isValidPhone = validatePhoneNumber(phoneNumber);

            if (!isValidId) {
                System.out.println("Invalid ID for user: " + name + ", ID: " + id);
                invalidUsers.add(name + " (Invalid ID)");
                continue; // Skip adding user if ID is invalid
            }

            if (!isValidPhone) {
                System.out.println("Invalid Phone number for user: " + name + ", Phone: " + phoneNumber);
                invalidUsers.add(name + " (Invalid Phone)");
                continue; // Skip adding user if phone is invalid
            }

            //if user already exist update new parameter
            userMap.put(id, new User(id, name, phoneNumber,address));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Users added successfully.");
        if (!invalidUsers.isEmpty()) {
            response.put("invalidUsers", invalidUsers);
        }
        return response;
    }

    /**
     * Validates an Israeli ID number using a checksum algorithm.
     * Ensures ID is 9 digits, with a valid check digit.
     *
     * @param ID number as a string
     * @return true if valid, false otherwise
     */
    private boolean validateIsraeliId(String id) {
        if (id.length() < 8 || id.length() > 9 || !id.matches("\\d+")) return false;

        id = String.format("%9s", id).replace(' ', '0');

        int sum = 0;
        int num;
        for (int i = 0; i < 9; i++) {
            num = Character.getNumericValue(id.charAt(i));
            if (i % 2 != 0) {
                num *= 2;
            }
            if (num > 9) {
                sum += num - 9;
            } else {
                sum += num;
            }
        }
        return sum % 10 == 0;
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        return Pattern.matches("^(05\\d{8}|0[23489]\\d{7})$", phoneNumber);
    }
    /**
     * Validates a phone number based on Israeli numbering format.
     *
     * @param phoneNumber Phone number as a string.
     * @return true if valid, false otherwise.
     */
    public String getUserInfoByName(String searchName){
        if (userMap == null || userMap.isEmpty()) {
            return "{}"; // Return empty JSON if map is empty
        }

        ObjectMapper mapper = new ObjectMapper();
        for (User user : userMap.values()) {
            if (user.getName().equalsIgnoreCase(searchName)) {
                ObjectNode jsonResult = mapper.createObjectNode();
                jsonResult.put("id", user.getId());
                jsonResult.put("name", user.getName());
                jsonResult.put("phoneNumber", user.getPhoneNumber());
                jsonResult.put("address", user.getAddress());
                try {
                return mapper.writeValueAsString(jsonResult);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();  // Print the error for debugging
                    System.err.println("Error processing JSON: " + e.getMessage());
                }
            }
        }
        return "{}"; // No user found
    }

    /**
     * Searches for a user by name and returns their details as a JSON string.
     *
     * @param searchName The name of the user to search for.
     * @return JSON string containing user details, or an empty JSON object if not found.
     */
    public String getAllUserNamesAsJson() {
        if (userMap == null || userMap.isEmpty()) {
            return "[]"; // Return empty JSON array if map is empty
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();

        for (User user : userMap.values()) {
            jsonArray.add(user.getName()); // Add only the name
        }
        try {
            return mapper.writeValueAsString(jsonArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();  // Print the error for debugging
            System.err.println("Error processing JSON: " + e.getMessage());
        }
        return "[]";
    }
}


