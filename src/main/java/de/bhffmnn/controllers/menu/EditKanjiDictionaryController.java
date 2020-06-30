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
import java.util.ArrayList;
import java.util.ResourceBundle;

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

    private TableColumn<String, Kanji> charClmn;
    private TableColumn<String, Kanji> onClmn;
    private TableColumn<String, Kanji> kunClmn;
    private TableColumn<String, Kanji> meanClmn;
    private TableColumn<String, Kanji> mnemClmn;
    private TableColumn<String, Kanji> charLvlClmn;
    private TableColumn<String, Kanji> charDueClmn;
    private TableColumn<String, Kanji> readLvlClmn;
    private TableColumn<String, Kanji> readDueClmn;
    private TableColumn<String, Kanji> meanLvlClmn;
    private TableColumn<String, Kanji> meanDueClmn;

    //This exists so you can reset the dictionary
    private KanjiDictionary cloneDictionary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kanjiFilter = new KanjiFilter();

        cloneDictionary = App.kanjiDictionary.cloneDeep();

        charClmn = createColumn("character", "character");
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

        for (Kanji k : cloneDictionary) {
            dictTable.getItems().add(k);
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
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/addKanji.fxml"));

        AddKanjiController controller = new AddKanjiController(cloneDictionary);
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
            Kanji kanji = (Kanji) dictTable.getItems().get(position.getRow());
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/menu/editKanji.fxml"));

            EditKanjiController controller = new EditKanjiController(kanji);
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
            Kanji kanji = (Kanji) dictTable.getItems().get(position.getRow());
            cloneDictionary.remove(kanji);
            reloadTable();
        }

    }
    @FXML
    private void saveButtonAction(ActionEvent actionEvent) throws Exception {
        App.kanjiDictionary = cloneDictionary.cloneDeep();
        App.kanjiDictionary.save(App.settings.getKanjiDictionaryFilePath());
    }

    @FXML
    private void resetButtonAction(ActionEvent actionEvent) throws Exception {
        cloneDictionary = App.kanjiDictionary.cloneDeep();
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

    private TableColumn<String, Kanji> createColumn(String title, String feature) {
        TableColumn<String, Kanji> newColumn = new TableColumn<>(title);
        newColumn.setCellValueFactory(new PropertyValueFactory<>(feature));
        return newColumn;
    }

    private void reloadTable() {
        dictTable.getItems().clear();

        KanjiDictionary filteredDictionary = kanjiFilter.filter(cloneDictionary);

        for (Kanji k : filteredDictionary) {
            dictTable.getItems().add(k);
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

        public KanjiDictionary filter(KanjiDictionary kanjiDictionary) {
            KanjiDictionary filteredDictionary = kanjiDictionary.clone();
            if (!(charFilter == null)) {
                filteredDictionary = new KanjiDictionary();
                filteredDictionary.add(kanjiDictionary.getKanjiByCharacter(charFilter));
            }
            if (!(onFilter == null)) {
                ArrayList<Kanji> removalList = new ArrayList<>();
                for (Kanji k : filteredDictionary) {
                    if (!k.getOnReading().contains(onFilter)) {
                        removalList.add(k);
                    }
                }
                filteredDictionary.removeAll(removalList);
            }
            if (!(kunFilter == null)) {
                ArrayList<Kanji> removalList = new ArrayList<>();
                for (Kanji k : filteredDictionary) {
                    if (!k.getKunReading().contains(kunFilter)) {
                        removalList.add(k);
                    }
                }
                filteredDictionary.removeAll(removalList);
            }
            if (!(meanFilter == null)) {
                ArrayList<Kanji> removalList = new ArrayList<>();
                for (Kanji k : filteredDictionary) {
                    if (!k.getMeaning().contains(meanFilter)) {
                        removalList.add(k);
                    }
                }
                filteredDictionary.removeAll(removalList);
            }
            if (!(charLvlFilter == null)) {
                KanjiDictionary removalList = filteredDictionary.clone();
                removalList.removeAll(filteredDictionary.getByCharacterLevel(charLvlFilter));
                filteredDictionary.removeAll(removalList);
            }
            if (!(readLvlFilter == null)) {
                KanjiDictionary removalList = filteredDictionary.clone();
                removalList.removeAll(filteredDictionary.getByCharacterLevel(readLvlFilter));
                filteredDictionary.removeAll(removalList);
            }
            if (!(meanLvlFilter == null)) {
                KanjiDictionary removalList = filteredDictionary.clone();
                removalList.removeAll(filteredDictionary.getByCharacterLevel(meanLvlFilter));
                filteredDictionary.removeAll(removalList);
            }
            if (!(charDueFilter == null)) {
                ArrayList<Kanji> removalList = new ArrayList<>();
                for (Kanji k : filteredDictionary) {
                    if (k.getCharacterDue().isAfter(charDueFilter.plusDays(1))) {
                        removalList.add(k);
                    }
                }
                filteredDictionary.removeAll(removalList);
            }
            if (!(readDueFilter == null)) {
                ArrayList<Kanji> removalList = new ArrayList<>();
                for (Kanji k : filteredDictionary) {
                    if (k.getCharacterDue().isAfter(readDueFilter.plusDays(1))) {
                        removalList.add(k);
                    }
                }
                filteredDictionary.removeAll(removalList);
            }
            if (!(meanDueFilter == null)) {
                ArrayList<Kanji> removalList = new ArrayList<>();
                for (Kanji k : filteredDictionary) {
                    if (k.getCharacterDue().isAfter(meanDueFilter.plusDays(1))) {
                        removalList.add(k);
                    }
                }
                filteredDictionary.removeAll(removalList);
            }
            return filteredDictionary;
        }
    }
}