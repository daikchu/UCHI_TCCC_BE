package com.vn.osp.notarialservices.contract.dto;

public class ContractKeysDissectFile {
    private String key;
    private String nextKey;
    private Integer flg_inline;
    private String type;
    // private Integer time;
    private String firstWord;//get value after this word
    private String endWord;//
    private String map_var;//get value after this word
    private String contract_template_id;//
    private Integer type_property;//
    private String type_key; //type of key: duong su/tai san/chung

    public ContractKeysDissectFile(String key, String nextKey, Integer flg_inline, String type, String firstWord, String endWord, String map_var, String contract_template_id, Integer type_property) {
        this.key = key;
        this.nextKey = nextKey;
        this.flg_inline = flg_inline;
        this.type = type;
        this.firstWord = firstWord;
        this.endWord = endWord;
        this.map_var = map_var;
        this.contract_template_id = contract_template_id;
        this.type_property = type_property;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNextKey() {
        return nextKey;
    }

    public void setNextKey(String nextKey) {
        this.nextKey = nextKey;
    }

    public Integer getFlg_inline() {
        return flg_inline;
    }

    public void setFlg_inline(Integer flg_inline) {
        this.flg_inline = flg_inline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }

    public String getEndWord() {
        return endWord;
    }

    public void setEndWord(String endWord) {
        this.endWord = endWord;
    }

    public String getMap_var() {
        return map_var;
    }

    public void setMap_var(String map_var) {
        this.map_var = map_var;
    }

    public String getContract_template_id() {
        return contract_template_id;
    }

    public void setContract_template_id(String contract_template_id) {
        this.contract_template_id = contract_template_id;
    }

    public Integer getType_property() {
        return type_property;
    }

    public void setType_property(Integer type_property) {
        this.type_property = type_property;
    }
}
