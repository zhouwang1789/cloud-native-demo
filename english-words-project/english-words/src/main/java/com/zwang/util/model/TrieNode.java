package com.zwang.util.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Getter
@Setter
@Slf4j
public class TrieNode {

    private TrieNode[] children;
    private String word;
    private int times;
    private int topN;
    private List<TrieNode> topNList;

    public TrieNode(@ConfigProperty(name = "words.topn") int topN) {
        this.children = new TrieNode[128];
        this.word = null;
        this.times = 0;
        this.topN = topN;
        this.topNList = new ArrayList<>();
    }

    public void add(String word) {
        if (StringUtils.isBlank(word)) {
            throw new IllegalArgumentException(String.format("Invalid word:[%s]", word));
        }

        log.debug("Adding word:[{}]", word);
        TrieNode cur = this;
        List<TrieNode> visited = new ArrayList<>();

        for (char c : word.toCharArray()) {
            if (cur.getChildren()[c] == null) {
                cur.getChildren()[c] = new TrieNode(this.topN);
            }

            cur = cur.getChildren()[c];
            visited.add(cur);
        }

        // cur is leaf node now
        cur.setWord(word);
        cur.setTimes(cur.getTimes() + 1);

        // update the nodes along the path from root to cur
        for (TrieNode node : visited) {
            node.update(cur);
        }

    }

    public void update(TrieNode leaf) {
        log.debug("Updating leaf node:[{}]", leaf);
        if (!this.topNList.contains(leaf)) {
            this.topNList.add(leaf);
        }

        this.topNList.sort(Comparator.comparing((TrieNode n) -> n.times).reversed().thenComparing(n -> n.word));

        if (this.topNList.size() > this.topN) {
            this.topNList.remove(topNList.size() - 1);
        }
    }

    public List<String> find(String prefix) {
        List<String> res = new ArrayList<>();
        if (StringUtils.isBlank(prefix)) {
            log.debug("Prefix is empty, so return");
            return res;
        }

        TrieNode cur = this;
        for (char c : prefix.toCharArray()) {
            if (cur.children[c] != null) {
                cur = cur.children[c];
            } else {
                return res;
            }
        }

        return cur.getTopNList().stream().map(TrieNode::getWord).collect(Collectors.toList());
    }

}
