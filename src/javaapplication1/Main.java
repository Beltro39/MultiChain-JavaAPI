/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1;
import multichain.command.*;
import java.util.List;
import java.util.ArrayList;
import multichain.object.*;
import com.google.gson.internal.LinkedTreeMap;
/**
 *
 * @author sbeltrana
 * 
 * 
 
 7*/

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import java.lang.reflect.Type;
//import com.google.gson.reflect.TypeToken;
public class Main {
     static CommandManager commandManager;
     public static void main(String[] args) {
         
        commandManager= new CommandManager("localhost", "9724", "multichainrpc", "Ad8ajj7JMinQo9rLEpK9Hh3Tygyz7QHoQ7R8NujKhdvv");
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                  
        
         
           // addressResult = commandManager.invoke(CommandElt.LISTSTREAMITEMS, "comunidad", "individuos");
           //List<Permission> listPermissions = (List<Permission>) commandManager.invoke(CommandElt.LISTPERMISSIONS);
         //  List<StreamKeyItem> listItems = (List<StreamKeyItem>) commandManager.invoke(CommandElt.LISTSTREAMITEMS, "individuos");
        
                new homeUI().setVisible(true);
            }
        });
    }
     
     public static String getNombre(String numero){
         String nombre= "";  
           try {
              
         List<StreamKeyItem> listItems = (List<StreamKeyItem>) commandManager.invoke(CommandElt.LISTSTREAMITEMS, "individuos");
          for(int i= 0; i<listItems.size(); i++){
            List<String> list = listItems.get(i).getKeys();
            if(list.contains(numero)){
              LinkedTreeMap tree = (LinkedTreeMap) listItems.get(i).getData();
              LinkedTreeMap tree2= (LinkedTreeMap) tree.get("json");   
              nombre= (String) tree2.get("nombre");
               return nombre;
            }
          }
           } catch (MultichainException e) {
            e.printStackTrace();
            
        }
           return nombre;
    
     }
     
     public static List<List<String>> cargarAntecedentesPorDelito(String numeroDelito){
         String j= "0";     
         if(numeroDelito=="Leve"){
           j= "1";
         }else if(numeroDelito== "Comun"){
           j= "2";
         }else{
           j= "3";
         }  
         
         List<List<String>> array = new ArrayList<>();
            try {
       
         List<StreamKeyItem> listItems = (List<StreamKeyItem>) commandManager.invoke(CommandElt.LISTSTREAMITEMS, "antecedentes");

         
         for(int i= 0; i<listItems.size(); i++){
             List<String> lista= new ArrayList<>();
            List<String> list = listItems.get(i).getKeys();
            if(list.contains(j)){
              String numero = list.get(0);
              String nombre= getNombre(numero);  
              LinkedTreeMap tree = (LinkedTreeMap) listItems.get(i).getData();
                System.out.println(tree+ "  "+ i);
              LinkedTreeMap tree2= (LinkedTreeMap) tree.get("json");
              lista.add(nombre);
              lista.add((String)tree2.get("delito"));
              lista.add((String)tree2.get("descripcion"));  
              array.add(lista); 
            }
          
         }
                System.out.println(array);
                
         } catch (MultichainException e) {
            e.printStackTrace();
            
        }
            return array;
     
     }
     
     public static List<List<String>> cargarAntecendetesPorIdentificacion (String numero){
          List<List<String>> array = new ArrayList<>();
            try {
         String nombre= getNombre(numero);
                
         List<StreamKeyItem> listItems = (List<StreamKeyItem>) commandManager.invoke(CommandElt.LISTSTREAMITEMS, "antecedentes");
       
        
         for(int i= 0; i<listItems.size(); i++){
               List<String> lista= new ArrayList<>();
            List<String> list = listItems.get(i).getKeys();
            if(list.contains(numero)){
              LinkedTreeMap tree = (LinkedTreeMap) listItems.get(i).getData();
              LinkedTreeMap tree2= (LinkedTreeMap) tree.get("json");
              lista.add(nombre);
              lista.add((String)tree2.get("delito"));
              lista.add((String)tree2.get("descripcion"));  
              array.add(lista);
            }
          
         }
        
         
         } catch (MultichainException e) {
            e.printStackTrace();
            
        }
            return array;
     }
     
     public static List<String> cargarIndividuos(){
          List<String> list= new ArrayList<String>();
          try {
          List<StreamKey> listItems = (List<StreamKey>) commandManager.invoke(CommandElt.LISTSTREAMKEYS, "individuos");
         
          for(int i= 0; i<listItems.size(); i++){
              list.add(listItems.get(i).getKey());     
          }
            
         
           } catch (MultichainException e) {
            e.printStackTrace();
            
        }
          return list;
     }
     
     public static void registrarAntecedente(String key, String tipoDelito, String delito, String descripcion){
          try {
         String i= "0";     
         if(tipoDelito=="Leve"){
           i= "1";
         }else if(tipoDelito== "Comun"){
           i= "2";
         }else{
           i= "3";
         }     
         LinkedTreeMap mytree = new LinkedTreeMap();
         mytree.put("delito", delito);
         mytree.put("descripcion", descripcion);
         LinkedTreeMap mytreeJson= new LinkedTreeMap();
         mytreeJson.put("json", mytree);
         List<String> list= new ArrayList<String>();
         list.add(key);
         list.add(i);
         commandManager.invoke(CommandElt.PUBLISH, "antecedentes", list , mytreeJson);
          } catch (MultichainException e) {
            e.printStackTrace();
            
        }
     }
    
     public static void registrarIndividuo(String key, String nombre){
        try {
           // addressResult = commandManager.invoke(CommandElt.LISTSTREAMITEMS, "comunidad", "individuos");
           //List<Permission> listPermissions = (List<Permission>) commandManager.invoke(CommandElt.LISTPERMISSIONS);
         //  List<StreamKeyItem> listItems = (List<StreamKeyItem>) commandManager.invoke(CommandElt.LISTSTREAMITEMS, "individuos");
    
         
           LinkedTreeMap mytree = new LinkedTreeMap();
           mytree.put("nombre", nombre);
           LinkedTreeMap mytreeJson= new LinkedTreeMap();
           mytreeJson.put("json", mytree);
           commandManager.invoke(CommandElt.PUBLISH, "individuos", key, mytreeJson);
        /* 
          Object result = null;
           List<StreamKeyItem> items = new ArrayList<>();
           //final Object[] params = new Object[] { "stream_name", Boolean.TRUE, 10, 0 };
           result = commandManager.invoke(CommandElt.LISTSTREAMITEMS, "individuos");
           items = (ArrayList<StreamKeyItem>) result;
           LinkedTreeMap tree =(LinkedTreeMap) items.get(0).getData();
           LinkedTreeMap tree2 =(LinkedTreeMap) tree.get("json");
            System.out.println(tree2.get("nombre"));
            System.out.println(items.get(0).getKeys());
           
          */
           //final Object[] params = new Object[] { "stream_name", Boolean.TRUE, 10, 0 };
          // commandManager.invoke(CommandElt.PUBLISH, "individuos", "KEY111", items.get(0).getData());
          // items = (ArrayList<StreamKeyItem>) result;
          //  System.out.println(items.get(0).getData().toString());
           //String dataStringCut= dataString.substring(6, dataString.length()-1);
           // System.out.println(dataStringCut);
           // JsonParser parser = new JsonParser();  
           // JsonObject json = (JsonObject) parser.parse(dataStringCut);
           // System.out.println(json);
           // System.out.println(listPermissions);
           // System.out.println(listItems.get(0).getData().getClass().getName());
            //Type listType = new TypeToken<ArrayList<MyData>>() {}.getType();
        } catch (MultichainException e) {
            e.printStackTrace();
            
        }
     
     }
     
}
