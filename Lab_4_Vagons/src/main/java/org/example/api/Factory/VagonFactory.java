package org.example.api.Factory;

import org.example.api.Dto.VagonDTO;
import org.example.persistence.Repositories.AbstractStorage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VagonFactory extends AbstractStorage<VagonDTO> {

    private static VagonFactory instance;

    private VagonFactory() {}

    public static VagonFactory getInstance() {
        if (instance == null) {
            instance = new VagonFactory();
        }
        return instance;
    }

    @Override
    public void readFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    int cost = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String desc = parts[2];

                    VagonDTO vagon = new VagonDTO(cost, name, desc);
                    addToListStorage(vagon);
                    addToMapStorage(cost, vagon);
                }
                catch (Exception e1)
                {
                    continue;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (VagonDTO bus : listStorage) {
                bw.write(bus.getCost() + "," +
                        bus.getName() + "," +
                        bus.getDescription()+"\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VagonDTO> readFromXml(String filename) {
        List<VagonDTO> list = new ArrayList<>();
        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);

            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("vagon");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    VagonDTO vagon = new VagonDTO();
                    vagon.setCost(Integer.parseInt(element.getElementsByTagName("cost").item(0).getTextContent()));
                    vagon.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    vagon.setDescription(element.getElementsByTagName("description").item(0).getTextContent());

                    list.add(vagon);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void writeToXml(String filename, List<VagonDTO> list) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("vagon");
            document.appendChild(root);

            for (VagonDTO vehicle : list) {
                Element vagon = document.createElement("vagon");

                Element cost = document.createElement("cost");
                cost.appendChild(document.createTextNode(String.valueOf(vehicle.getCost())));
                vagon.appendChild(cost);

                Element type = document.createElement("name");
                type.appendChild(document.createTextNode(vehicle.getName()));
                vagon.appendChild(type);

                Element model = document.createElement("description");
                model.appendChild(document.createTextNode(vehicle.getDescription()));
                vagon.appendChild(model);

                root.appendChild(vagon);
            }

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("vagon.xml");
            StreamResult result = new StreamResult(new File(filename));

            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VagonDTO findByName(String name) {
        return listStorage.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(new VagonDTO(-1,"",""));
    }

    public List<VagonDTO> readDataFromJsonFile(String fileName) {
        List<VagonDTO> vagon = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                VagonDTO vagon1 = new VagonDTO();
                vagon1.setCost(jsonObject.getInt("cost"));
                vagon1.setName(jsonObject.getString("name"));
                vagon1.setDescription(jsonObject.getString("description"));
                vagon.add(vagon1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vagon;
    }

    public void writeDataToJsonFile(String fileName, List<VagonDTO> vagons) {
        JSONArray jsonArray = new JSONArray();
        for (VagonDTO vagon : vagons) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cost", vagon.getCost());
            jsonObject.put("name", vagon.getName());
            jsonObject.put("description", vagon.getDescription());
            jsonArray.put(jsonObject);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setListStorage(List<VagonDTO> vagonDTOS) {
        // Очистим существующие хранилища, чтобы не смешивать данные
        listStorage.clear();
        mapStorage.clear();

        // Добавим новые данные в оба хранилища
        for (VagonDTO vagon : vagonDTOS) {
            addToListStorage(vagon); // Предполагается, что этот метод добавляет в listStorage
            addToMapStorage(vagon.getCost(), vagon); // Предполагается, что этот метод добавляет в mapStorage
        }
    }
}
