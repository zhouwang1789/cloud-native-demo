package com.zwang.util.controller;

import com.zwang.util.service.EnglishWordsService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/words")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class EnglishWordsController {

    @Inject
    EnglishWordsService englishWordsService;

    @GET
    @Path("/{prefix}")
    public Response findWordsByPrefix(@PathParam("prefix") String prefix) {
        log.debug("Processing findWordsByPrefix request for prefix:[{}]", prefix);
        return Response.ok(englishWordsService.find(prefix))
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

}
