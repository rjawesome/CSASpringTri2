package com.nighthawk.spring_portfolio.mvc.flashcards;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.google.gson.Gson;

public class Quizlet {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private String id;
    public Quizlet (String id) {
        this.id = id;
    }

    public CompletableFuture<List<FlashcardContainer>> fetchQuizlet() {
        return httpClient.sendAsync(
                HttpRequest.newBuilder()
                        .GET()
                        .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                        .uri(URI.create(
                                "https://quizlet.com/webapi/3.4/studiable-item-documents?filters%5BstudiableContainerId%5D="
                                        + id
                                        + "&filters%5BstudiableContainerType%5D=1&perPage=200&page=1"))
                        .build(),
                HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(arg0 -> {
                    try {
                        return extractTerms(arg0);
                    } catch (IOException e) {
                        return new ArrayList<>();
                    } catch (InterruptedException e) {
                        return new ArrayList<>();
                    }
                });
    }

    private List<FlashcardContainer> extractTerms(String response) throws IOException, InterruptedException {
        Gson gson = new Gson();
        ResponseData res = gson.fromJson(response, ResponseData.class);
        List<FlashcardContainer> terms = res.responses.get(0).models.studiableItem;
    
        int currentLength = 5;
        String token = res.responses.get(0).paging.token;
        int page = 2;
        while (currentLength >= 200) {
            HttpResponse<String> httpResponse = httpClient.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(
                                    "https://quizlet.com/webapi/3.4/studiable-item-documents?filters%5BstudiableContainerId%5D="
                                            + id
                                            + "&filters%5BstudiableContainerType%5D=1&perPage=200&page="
                                            + page++
                                            + "&pagingToken="
                                            + token))
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
            String newResponse = httpResponse.body();
            ResponseData newRes = gson.fromJson(newResponse, ResponseData.class);
            terms.addAll(newRes.responses.get(0).models.studiableItem);
            currentLength = newRes.responses.get(0).models.studiableItem.size();
            token = newRes.responses.get(0).paging.token;
        }
    
        return terms;
    }

    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<List<FlashcardContainer>> future = new Quizlet("213648175").fetchQuizlet();
        List<FlashcardContainer> terms = future.join();
        terms.forEach(term -> {
          System.out.println(term.cardSides.get(0).media.get(0).plainText + ": " + term.cardSides.get(1).media.get(0).plainText);
        });
    }
    
    private static class ResponseData {
        List<Response> responses;
    }

    private static class Response {
        Paging paging;
        Models models;
    }

    private static class Paging {
        String token;
    }

    private static class Models {
        List<FlashcardContainer> studiableItem;
    }

    private static class FlashcardContainer {
      List<FlashcardSide> cardSides;
    }

    private static class FlashcardSide {
      List<FlashcardMedia> media;
      String label;
    }

    private static class FlashcardMedia {
      String plainText;
      String richText;
    }
}