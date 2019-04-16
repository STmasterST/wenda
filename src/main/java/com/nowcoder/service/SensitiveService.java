package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用字典树
 * 实现敏感词的过滤
 * 要特别注意读取敏感词文件出现的乱码问题
 * InputStreamReader read = new InputStreamReader(is,"UTF-8");
 */
@Service
public class SensitiveService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        root = new TrieNode();
//
//        try {
//            InputStream is = Thread.currentThread().getContextClassLoader()
//                    .getResourceAsStream("SensitiveWords.txt");
//            InputStreamReader read = new InputStreamReader(is);
//            BufferedReader bufferedReader = new BufferedReader(read);
//            String lineTxt;
//            while ((lineTxt = bufferedReader.readLine()) != null) {
//                lineTxt = lineTxt.trim();
//                addWord(lineTxt);
//            }
//            read.close();
//        } catch (Exception e) {
//            logger.error("读取敏感词文件失败" + e.getMessage());
//        }
//    }

    //初始化字典树
    static {
        root = new TrieNode();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
//                System.out.println(lineTxt);
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    //向字典树中添加关键敏感词
    private static void addWord(String lineText){
        TrieNode tempNode = root;
        for(int i = 0; i < lineText.length(); i++){
            Character c = lineText.charAt(i);
            TrieNode node = tempNode.getSubNode(c);//判断字典树中是否存在关键敏感词

            //如果当前节点下不包含敏感词,则添加对应的字符
            if(node == null){
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }

            tempNode = node;

            if (i == lineText.length() - 1) {
                // 关键词结束， 设置结束标志
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    //实现字典树,用来过滤敏感词
    private static class TrieNode{
        private boolean end = false;//表示以这个词结尾的节点是否为敏感词

        //当前结点下所有的子节点 ab ac ad
        private Map<Character,TrieNode> subNode = new HashMap<>();

        public void addSubNode(Character key,TrieNode node){
            subNode.put(key,node);
        }

        public TrieNode getSubNode(Character key){
            return subNode.get(key);
        }

        public boolean isKeyWordEnd(){
            return end;
        }

        public void setKeyWordEnd(boolean end){
            this.end = end;
        }
    }

    private static  TrieNode root;//字典树的根



    //过滤文本
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }
        String replacement = "***";
        TrieNode tempNode = root;//一开始指向根节点
        int begin = 0;
        int position = 0;
        StringBuilder sb = new StringBuilder();//用来拼接句子中不含敏感词的字符,并将敏感词替换为***

        //当这个指针还没有指到结尾处
        while(position < text.length()){
            char c = text.charAt(position);
            if(isSymbol(c)){//如果为非法字符
                if(tempNode == root){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            //如果当前访问的字符不在字典树中
            if(tempNode == null){
                sb.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = root;
            }else if(tempNode.isKeyWordEnd()){//发现敏感词
                sb.append(replacement);//将敏感词打码
                position = position + 1;
                begin = position;
                tempNode = root;
            }else{
                position++;
            }
        }

        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断敏感词中间的特殊字符
    private boolean isSymbol(char c){
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

//    public static void main(String[] args) {
//
//        System.out.println(new SensitiveService().filter("ni色情"));
//    }
}
