package com.automationanywhere.botcommand.samples.commands.utils;

import com.automationanywhere.botcommand.data.model.Schema;

import java.util.ArrayList;
import java.util.List;

public class FindInListSchema implements Cloneable{

    public List<Schema> schemas;

    //public FindInListSchema(List<Schema> objectList) { this.schemas = objectList; }

    public FindInListSchema(List<?> objectList) {
        if(objectList.size() >0) {
            if(objectList.get(0) instanceof String ) {
                List<String> stringList = this.cast(objectList);
                List<Schema> listSchema = new ArrayList();
                for (String sc : stringList) {
                    listSchema.add(new Schema(sc));
                }
                this.schemas = listSchema;
            }else{
                this.schemas = this.cast(objectList);
            }
        }
    }


    public List<Integer>schemasIdx(){
        List<Integer> lista = new ArrayList();
        int idx = 0;
        for(Schema lsc : this.schemas){
            lista.add(idx);
            idx++;
        }
        return lista;

    }
    public Boolean exists(String value) {
        for(Schema lsc : this.schemas){
            if(lsc.getName().equals(value)){
                return true;
            }
        }
        return false;
    }
    public int indexSchema(String value) {
        int i = 0;
        for(Schema lsc : this.schemas){
            if(lsc.getName().equals(value)){
                return i;
            }
            i++;
        }
        return -1;
    }
    public List<Integer> indexSchema(List<String> value) {
        List<Integer> lista = new ArrayList();
        for(String v: value){
            int i = 0;
            for(Schema lsc : this.schemas) {
                if (lsc.getName().equals(v)) {
                    lista.add(i);
                }
                i++;
            }
        }
        return lista;
    }
    public List<String> shemaNames() {
        List<String> names = new ArrayList();


        for(Schema lsc : this.schemas){
            names.add(lsc.getName());
        }
        return names;
    }
    public void addSchema(Integer idx,String SchemaName) {
        Schema novo = new Schema(SchemaName);
        this.schemas.add(idx,novo);
    }
    public void addSchema(String SchemaName) {
        Schema novo = new Schema(SchemaName);
        this.schemas.add(novo);
    }
    public void deleteSchema(Integer idx) {
        this.schemas.remove(idx);
    }
    public void deleteSchema(List<Integer> listIdx) {
        List<Schema> out = new ArrayList<>();
        int i = 0;

        for(Schema lsc : this.schemas) {
            if(!listIdx.contains(i)) {
                out.add(lsc);
            }
            i++;
        }
        this.schemas = out;
    }
    public void alterSchema(Integer idx,String SchemaName) {
        Schema novo = new Schema(SchemaName);
        this.schemas.set(idx,novo);
    }
    public static <T> List<T> cast(List list) {
        return list;
    }

    public FindInListSchema getClone() {
        try {
            // call clone in Object.
            return (FindInListSchema) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println (" Cloning not allowed. " );
            return this;
        }
    }

}
