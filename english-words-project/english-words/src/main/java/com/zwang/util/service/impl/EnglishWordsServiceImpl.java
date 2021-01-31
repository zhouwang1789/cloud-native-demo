package com.zwang.util.service.impl;

import com.zwang.util.model.TrieNode;
import com.zwang.util.service.EnglishWordsService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
@Slf4j
public class EnglishWordsServiceImpl implements EnglishWordsService {

    @Inject
    TrieNode trieNode;

    @Override
    public void add(String word) {
        log.debug("Adding word:[{}]", word);
        trieNode.add(word);
    }

    @Override
    public List<String> find(String prefix) {
        log.debug("Finding words for:[{}]", prefix);
        return trieNode.find(prefix);
    }
}
