package com.zwang.util;

import com.zwang.util.model.TrieNode;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class InitializeTrieNode {

    @ConfigProperty(name = "words.filename", defaultValue = "wordsAlpha.txt")
    String wordsFilename;

    @Inject
    TrieNode trieNode;

    void onStart(@Observes StartupEvent event) {
        initializeTrieNode();
    }

    private void initializeTrieNode() {
        log.info("Initializing trieNode from [{}]", this.wordsFilename);
        Instant start = Instant.now();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(this.wordsFilename);
        InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> words = bufferedReader.lines()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        log.info("Received words count:[{}], trieNode:[{}]", words.size(), trieNode);

        words.forEach(trieNode::add);
        log.info("Initialized trieNode with words count:[{}] in {} ms", words.size(), Duration.between(start, Instant.now()).toMillis());
    }
}