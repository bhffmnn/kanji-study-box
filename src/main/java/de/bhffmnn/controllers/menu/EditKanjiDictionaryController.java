/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.menu;

import de.bhffmnn.App;
import de.bhffmnn.models.Kanji;
import de.bhffmnn.models.KanjiDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

/**
 * Controller for the EditKanjiDictionary view that lets the user edit a KanjiDictionary object. The object is cloned
 * and the save button has to be used in order to overwrite the original object.
 */

public class EditKanjiDictionaryController implements Initializable {

    @FXML
    TableView dictTable;
    @FXML
    TextField charFilterField;
    @FXML
    TextField onFilterField;
    @FXML
    TextField kunFilterField;
    @FXML
    TextField meanFilterField;

    @FXML
    private Spinner<Integer> charLvlSpinner;
    @FXML
    private Spinner<Integer> readLvlSpinner;
    @FXML
    private Spinner<Integer> meanLvlSpinner;

    @FXML
    DatePicker charDuePicker;
    @FXML
    DatePicker readDuePicker;
    @FXML
    DatePicker meanDuePicker;

    @FXML
    CheckBox charCheckBox;
    @FXML
    CheckBox onCheckBox;
    @FXML
    CheckBox kunCheckBox;
    @FXML
    CheckBox meanCheckBox;
    @FXML
    CheckBox charLvlCheckBox;
    @FXML
    CheckBox charDueCheckBox;
    @FXML
    CheckBox readLvlCheckBox;
    @FXML
    CheckBox readDueCheckBox;
    @FXML
    CheckBox meanLvlCheckBox;
    @FXML
    CheckBox meanDueCheckBox;

    private KanjiFilter kanjiFilter;

    private TableColumn<String, KanjiMenuItem> indexClmn;
    private TableColumn<String, KanjiMenuItem> charClmn;
    private TableColumn<String, KanjiMenuItem> onClmn;
    private TableColumn<String, KanjiMenuItem> kunClmn;
    private TableColumn<String, KanjiMenuItem> meanClmn;
    private TableColumn<String, KanjiMenuItem> mnemClmn;
    private TableColumn<String, KanjiMenuItem> charLvlClmn;
    private TableColumn<String, KanjiMenuItem> charDueClmn;
    private TableColumn<String, KanjiMenuItem> readLvlClmn;
    private TableColumn<String, KanjiMenuItem> readDueClmn;
    private TableColumn<String, KanjiMenuItem> meanLvlClmn;
    private TableColumn<String, KanjiMenuItem> meanDueClmn;

    //This exists so you can reset the dictionary
    private KanjiMenuItemList kanjiMenuItems;
    private ArrayList<Integer> currentListIndexes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kanjiFilter = new KanjiFilter();

        kanjiMenuItems = new KanjiMenuItemList(App.kanjiDictionary);

        currentListIndexes = new ArrayList<>();

        indexClmn = createColumn("Index", "index");
        charClmn = createColumn("Character", "character");
        onClmn = createColumn("ON-Reading", "onReading");
        kunClmn = createColumn("kun-Reading", "kunReading");
        meanClmn = createColumn("Meaning", "meaning");
        mnemClmn = createColumn("Mnemonic", "mnemonic");

        charLvlClmn = createColumn("Character Level", "characterLevel");
        charDueClmn = createColumn("Character Due", "characterDue");
        readLvlClmn = createColumn("Reading Level", "readingLevel");
        readDueClmn = createColumn("Reading Due", "readingDue");
        meanLvlClmn = createColumn("Meaning Level", "meaningLevel");
        meanDueClmn = createColumn("Meaning Due", "meaningDue");

        dictTable.getColumns().add(indexClmn);
        dictTable.getColumns().add(charClmn);
        dictTable.getColumns().add(onClmn);
        dictTable.getColumns().add(kunClmn);
        dictTable.getColumns().add(meanClmn);
        dictTable.getColumns().add(mnemClmn);
        dictTable.getColumns().add(charLvlClmn);
        dictTable.getColumns().add(charDueClmn);
        dictTable.getColumns().add(readLvlClmn);
        dictTable.getColumns().add(readDueClmn);
        dictTable.getColumns().add(meanLvlClmn);
        dictTable.getColumns().add(meanDueClmn);

        int count = 0;
        for (KanjiMenuItem kmi : kanjiMenuItems) {
            dictTable.getItems().add(kmi);
            currentListIndexes.add(count);
            count++;
        }

        //Filter part
        charFilterField.setDisable(true);
        onFilterField.setDisable(true);
        kunFilterField.setDisable(true);
        meanFilterField.setDisable(true);

        charLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        charLvlSpinner.getValueFactory().setValue(0);
        charLvlSpinner.setDisable(true);
        readLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        readLvlSpinner.getValueFactory().setValue(0);
        readLvlSpinner.setDisable(true);
        meanLvlSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        meanLvlSpinner.getValueFactory().setValue(0);
        meanLvlSpinner.setDisable(true);

        charDuePicker.setValue(LocalDate.now());
        charDuePicker.setDisable(true);
        readDuePicker.setValue(LocalDate.now());
        readDuePicker.setDisable(true);
        meanDuePicker.setValue(LocalDate.now());
        meanDuePicker.setDisable(true);

        setUpCheckBox(charCheckBox, charFilterField);
        setUpCheckBox(onCheckBox, onFilterField);
        setUpCheckBox(kunCheckBox, kunFilterField);
        setUpCheckBox(meanCheckBox, meanFilterField);
        setUpCheckBox(charLvlCheckBox, charLvlSpinner);
        setUpCheckBox(charDueCheckBox, charDuePicker);
        setUpCheckBox(readLvlCheckBox, readLvlSpinner);
        setUpCheckBox(readDueCheckBox, readDuePicker);
        setUpCheckBox(meanLvlCheckBox, meanLvlSpinner);
        setUpCheckBox(meanDueCheckBox, meanDuePicker);
    }

    @FXML
    private void backButtonAction(ActionEvent actionEvent) throws Exception {
        Parent parent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML void addButtonAction(ActionEvent actionEvent) throws Exception {
        //TODO
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/addKanji.fxml"));

        AddKanjiMenuItemController controller = new AddKanjiMenuItemController(kanjiMenuItems);
        loader.setController(controller);

        Stage childStage = new Stage();
        childStage.initModality(Modality.WINDOW_MODAL);
        childStage.initOwner(dictTable.getScene().getWindow());
        childStage.setScene(new Scene((loader.load())));
        childStage.showAndWait();

        reloadTable();
    }

    @FXML void editButtonAction(ActionEvent actionEvent) throws Exception {
        if (!dictTable.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position = (TablePosition) dictTable.getSelectionModel().getSelectedCells().get(0);
            int itemIndex = ((KanjiMenuItem) dictTable.getItems().get(position.getRow())).getIndex();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/editKanjiMenuItem.fxml"));

            EditKanjiMenuItemController controller =
                    new EditKanjiMenuItemController(kanjiMenuItems, itemIndex);
            loader.setController(controller);

            Stage childStage = new Stage();
            childStage.initModality(Modality.WINDOW_MODAL);
            childStage.initOwner(dictTable.getScene().getWindow());
            childStage.setScene(new Scene((loader.load())));
            childStage.showAndWait();

            reloadTable();
        }
    }

    @FXML
    void deleteButtonAction(ActionEvent actionEvent) throws Exception {
        if (!dictTable.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position = (TablePosition) dictTable.getSelectionModel().getSelectedCells().get(0);
            KanjiMenuItem kmi = (KanjiMenuItem) dictTable.getItems().get(position.getRow());
            kanjiMenuItems.remove(kmi);
            reloadTable();
        }

    }
    @FXML
    private void saveButtonAction(ActionEvent actionEvent) throws Exception {
        KanjiDictionary kanjiDictionary = new KanjiDictionary();
        for (Kanji k : kanjiMenuItems) {
            kanjiDictionary.add(k);
        }
        App.kanjiDictionary = kanjiDictionary;
        App.kanjiDictionary.save(App.settings.getKanjiDictionaryFilePath());
    }

    @FXML
    private void resetButtonAction(ActionEvent actionEvent) throws Exception {
        kanjiMenuItems = new KanjiMenuItemList(App.kanjiDictionary);
        reloadTable();
    }

    @FXML
    private void applyButtonAction(ActionEvent actionEvent) throws Exception {
        if (charFilterField.isDisabled()) {
            kanjiFilter.setCharFilter(null);
        } else {
            kanjiFilter.setCharFilter(charFilterField.getText());
        }

        if (onFilterField.isDisabled()) {
            kanjiFilter.setOnFilter(null);
        } else {
            kanjiFilter.setOnFilter(onFilterField.getText());
        }

        if (kunFilterField.isDisabled()) {
            kanjiFilter.setKunFilter(null);
        } else {
            kanjiFilter.setKunFilter(kunFilterField.getText());
        }

        if (meanFilterField.isDisabled()) {
            kanjiFilter.setMeanFilter(null);
        } else {
            kanjiFilter.setMeanFilter(meanFilterField.getText());
        }

        if (charLvlSpinner.isDisabled()) {
            kanjiFilter.setCharLvlFilter(null);
        } else {
            kanjiFilter.setCharLvlFilter(charLvlSpinner.getValue());
        }

        if (readLvlSpinner.isDisabled()) {
            kanjiFilter.setReadLvlFilter(null);
        } else {
            kanjiFilter.setReadLvlFilter(readLvlSpinner.getValue());
        }

        if (meanLvlSpinner.isDisabled()) {
            kanjiFilter.setMeanLvlFilter(null);
        } else {
            kanjiFilter.setMeanLvlFilter(meanLvlSpinner.getValue());
        }

        if (charDuePicker.isDisabled()) {
            kanjiFilter.setCharDueFilter(null);
        } else {
            kanjiFilter.setCharDueFilter(charDuePicker.getValue());
        }

        if (readDuePicker.isDisabled()) {
            kanjiFilter.setReadDueFilter(null);
        } else {
            kanjiFilter.setReadDueFilter(readDuePicker.getValue());
        }

        if (meanDuePicker.isDisabled()) {
            kanjiFilter.setMeanDueFilter(null);
        } else {
            kanjiFilter.setMeanDueFilter(meanDuePicker.getValue());
        }

        reloadTable();
    }

    private TableColumn<String, KanjiMenuItem> createColumn(String title, String feature) {
        TableColumn<String, KanjiMenuItem> newColumn = new TableColumn<>(title);
        newColumn.setCellValueFactory(new PropertyValueFactory<>(feature));
        return newColumn;
    }

    private void reloadTable() {
        dictTable.getItems().clear();

        currentListIndexes = kanjiFilter.filter(kanjiMenuItems);

        for (int i : currentListIndexes) {
            dictTable.getItems().add(kanjiMenuItems.get(i));
        }
    }

    private void setUpCheckBox(CheckBox checkBox, Node node) {
        checkBox.selectedProperty().addListener(                ((observableValue, oldValue, newValue) -> {
            if (newValue) {
                node.setDisable(false);
            }
            else {
                node.setDisable(true);
            }
        }));
    }

    private class KanjiFilter {
        private String charFilter;
        private String onFilter;
        private String kunFilter;
        private String meanFilter;
        private Integer charLvlFilter;
        private Integer readLvlFilter;
        private Integer meanLvlFilter;
        private LocalDate charDueFilter;
        private LocalDate readDueFilter;
        private LocalDate meanDueFilter;

        private KanjiFilter() {
            charFilter = onFilter = kunFilter = meanFilter = null;
            charLvlFilter = readLvlFilter = meanLvlFilter = null;
            charDueFilter = readDueFilter = meanDueFilter = null;
        }

        public String getCharFilter() {
            return charFilter;
        }
        public String getKunFilter() {
            return kunFilter;
        }
        public String getMeanFilter() {
            return meanFilter;
        }
        public String getOnFilter() {
            return onFilter;
        }

        public Integer getCharLvlFilter() {
            return charLvlFilter;
        }
        public Integer getMeanLvlFilter() {
            return meanLvlFilter;
        }
        public Integer getReadLvlFilter() {
            return readLvlFilter;
        }

        public LocalDate getCharDueFilter() {
            return charDueFilter;
        }
        public LocalDate getMeanDueFilter() {
            return meanDueFilter;
        }
        public LocalDate getReadDueFilter() {
            return readDueFilter;
        }

        public void setCharFilter(String charFilter) {
            this.charFilter = charFilter;
        }
        public void setKunFilter(String kunFilter) {
            this.kunFilter = kunFilter;
        }
        public void setMeanFilter(String meanFilter) {
            this.meanFilter = meanFilter;
        }
        public void setOnFilter(String onFilter) {
            this.onFilter = onFilter;
        }

        public void setCharLvlFilter(Integer charLvlFilter) {
            this.charLvlFilter = charLvlFilter;
        }
        public void setReadLvlFilter(Integer readLvlFilter) {
            this.readLvlFilter = readLvlFilter;
        }
        public void setMeanLvlFilter(Integer meanLvlFilter) {
            this.meanLvlFilter = meanLvlFilter;
        }

        public void setCharDueFilter(LocalDate charDueFilter) {
            this.charDueFilter = charDueFilter;
        }
        public void setReadDueFilter(LocalDate readDueFilter) {
            this.readDueFilter = readDueFilter;
        }
        public void setMeanDueFilter(LocalDate meanDueFilter) {
            this.meanDueFilter = meanDueFilter;
        }

        public ArrayList<Integer> filter(KanjiMenuItemList kanjiMenuItems) {
            ArrayList<Integer> filteredIndexes = new ArrayList<>();
            for (int i = 0; i < kanjiMenuItems.size(); i++) {
                filteredIndexes.add(i);
            }
            if (!(charFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!kmi.getCharacter().equals(charFilter)) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(onFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!kmi.getOnReading().contains(onFilter)) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(kunFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!kmi.getKunReading().contains(kunFilter)) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(meanFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!kmi.getMeaning().contains(meanFilter)) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(charLvlFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!charLvlFilter.equals(kmi.getCharacterLevel())) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(readLvlFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!readLvlFilter.equals(kmi.getReadingLevel())) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(meanLvlFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!meanLvlFilter.equals(kmi.getMeaningLevel())) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(charDueFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!kmi.getCharacterDue().isAfter(charDueFilter.minusDays(1))) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(readDueFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!kmi.getReadingDue().isAfter(readDueFilter.minusDays(1))) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            if (!(meanDueFilter == null)) {
                for (KanjiMenuItem kmi : kanjiMenuItems) {
                    if (!kmi.getMeaningDue().isAfter(meanDueFilter.minusDays(1))) {
                        filteredIndexes.remove(Integer.valueOf(kanjiMenuItems.indexOf(kmi)));
                    }
                }
            }
            return filteredIndexes;
        }
    }

    public class KanjiMenuItemList extends AbstractCollection<KanjiMenuItem> {
        private ArrayList<KanjiMenuItem> kanjiMenuItems;

        public KanjiMenuItemList() {
            kanjiMenuItems = new ArrayList<>();
        }

        public KanjiMenuItemList(KanjiDictionary kanjiDictionary) {
            kanjiMenuItems = new ArrayList<>();
            for (Kanji k : kanjiDictionary) {
                kanjiMenuItems.add(new KanjiMenuItem(k, kanjiMenuItems.size()));
            }
        }

        public KanjiMenuItem get(int index) {
            return kanjiMenuItems.get(index);
        }

        @Override
        public boolean remove(Object o) {
            if (!kanjiMenuItems.contains(o)) {
                return false;
            }
            this.changeIndexOf((KanjiMenuItem) o, this.size() - 1);
            return kanjiMenuItems.remove(o);
        }

        public void changeIndexOf(KanjiMenuItem kanjiMenuItem, int newIndex) {
            if (!kanjiMenuItems.contains(kanjiMenuItem)) {
                throw new IllegalArgumentException("The KanjiMenuList object does not contain this KanjiMenuItem object");
            }
            if (newIndex > kanjiMenuItems.size() - 1 || newIndex < 0) {
                throw new IndexOutOfBoundsException();
            }
            int oldIndex = kanjiMenuItems.indexOf(kanjiMenuItem);
            if (newIndex == oldIndex) {
                return;
            }
            ArrayList<KanjiMenuItem> newList = new ArrayList<>();
            if (oldIndex < newIndex) {
                for (int i = 0; i < oldIndex; i++) {
                    newList.add(kanjiMenuItems.get(i));
                }
                for (int i = oldIndex + 1; i <= newIndex; i++) {
                    newList.add(kanjiMenuItems.get(i));
                    kanjiMenuItems.get(i).setIndex(i - 1);

                }
                newList.add(kanjiMenuItem);
                kanjiMenuItem.setIndex(newIndex);
                for (int i = newIndex + 1; i < kanjiMenuItems.size(); i++) {
                    newList.add(kanjiMenuItems.get(i));
                }
            }
            if (oldIndex > newIndex) {
                for (int i = 0; i < newIndex; i++) {
                    newList.add(kanjiMenuItems.get(i));
                }
                newList.add(kanjiMenuItem);
                kanjiMenuItem.setIndex(newIndex);
                for (int i = newIndex; i < oldIndex; i++) {
                    newList.add(kanjiMenuItems.get(i));
                    kanjiMenuItems.get(i).setIndex(i + 1);
                }
                for (int i = oldIndex + 1; i < kanjiMenuItems.size(); i++) {
                    newList.add(kanjiMenuItems.get(i));
                }
            }
            kanjiMenuItems = newList;
        }

        @Override
        public Iterator<KanjiMenuItem> iterator() {
            return kanjiMenuItems.iterator();
        }

        public int indexOf(KanjiMenuItem kanjiMenuItem) {
            return kanjiMenuItems.indexOf(kanjiMenuItem);
        }

        @Override
        public int size() {
            return kanjiMenuItems.size();
        }

        @Override
        public boolean contains(Object o) {
            return kanjiMenuItems.contains(o);
        }

        @Override
        public boolean add(KanjiMenuItem kanjiMenuItem) {
            return false;
        }

        public boolean addNewItem(Kanji kanji) {
            for (KanjiMenuItem kmi : kanjiMenuItems) {
                if (kmi.getCharacter().equals(kanji.getCharacter())) {
                    return false;
                }
            }
            kanjiMenuItems.add(new KanjiMenuItem(kanji, kanjiMenuItems.size()));
            return true;
        }
    }

    public class KanjiMenuItem extends Kanji {
        private int index;

        private KanjiMenuItem(Kanji kanji, int index) {
            super(kanji.getCharacter(), kanji.getOnReading(), kanji.getKunReading(), kanji.getMeaning(),
                    kanji.getMnemonic(), kanji.getCharacterLevel(), kanji.getCharacterDue(), kanji.getReadingLevel(),
                    kanji.getReadingDue(), kanji.getMeaningLevel(), kanji.getMeaningDue());
            this.index = index;
        }

        private void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}