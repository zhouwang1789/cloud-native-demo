package com.zwang.util.service;

import java.util.List;

public interface EnglishWordsService {

    void add(String word);

    List<String> find(String word);

}
