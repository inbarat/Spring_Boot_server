package com.example;

import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * It provides endpoints for creating users, fetching user names,
 * and retrieving a user's details by name.
 */

@RestController
@RequestMapping("/users")

public class APIController {
    private final BL server = new BL();

    @PostMapping
    public Map<String, Object> addUsers(@RequestBody String json) {
        return server.AddUserFromJson(json);
    }

    @GetMapping("/names")
    public String getAllUserNames() {
        return server.getAllUserNamesAsJson();
    }

    @GetMapping("/{name}")
    public String getUserByName(@PathVariable String name) {
        return server.getUserInfoByName(name);
    }
}