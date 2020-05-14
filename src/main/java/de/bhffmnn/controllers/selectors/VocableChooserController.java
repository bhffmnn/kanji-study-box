/*
 * Copyright (c) 2020, Benjamin Hoffmann
 * All rights reserved.

 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package de.bhffmnn.controllers.selectors;

import de.bhffmnn.App;
import de.bhffmnn.models.KanjiDictionary;
import de.bhffmnn.models.Vocable;
import de.bhffmnn.models.VocableDictionary;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class VocableChooserController implements Initializable {
    private KanjiDictionary kanjiDictionary;

    //Box containing the vocables
    @FXML
    private VBox theBox;

    //The labels for the kanji at the top
    @FXML
    private Label charLabel;
    @FXML
    private Label onLabel;
    @FXML
    private Label kunLabel;
    @FXML
    private Label meanLabel;

    //Progress bar
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    //Grids that are put into theBox
    private GridPane unstartedGrid;
    private GridPane startedGrid;

    //Vocables that shall be learned
    private VocableDictionary startList;

    //Index of the currently displayed kanji
    private IntegerProperty currentIndex;

    public VocableChooserController(KanjiDictionary kanjiDictionary) {
        this.kanjiDictionary = kanjiDictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentIndex = new SimpleIntegerProperty(0);
        startList = new VocableDictionary();
        currentIndex.addListener(
                ((observableValue, oldValue, newValue) -> {
                    theBox.getChildren().clear();

                    charLabel.setText(kanjiDictionary.getByIndex(newValue.intValue()).getCharacter());
                    onLabel.setText(kanjiDictionary.getByIndex(newValue.intValue()).getOnReading());
                    kunLabel.setText(kanjiDictionary.getByIndex(newValue.intValue()).getKunReading());
                    meanLabel.setText(kanjiDictionary.getByIndex(newValue.intValue()).getMeaning());

                    VocableDictionary kanjiVocab = App.vocableDictionary.getByCharacter(kanjiDictionary.getByIndex(newValue.intValue()).getCharacter());
                    startedGrid = createStartedGridPane(kanjiVocab);
                    if (startedGrid != null) {
                        theBox.getChildren().addAll(startedGrid);
                    }
                    unstartedGrid = createUnstartedGrid(kanjiVocab);
                    if (unstartedGrid != null) {
                        theBox.getChildren().addAll(unstartedGrid);
                    }

                    //update progress bar
                    progressBar.progressProperty().set((newValue.doubleValue() + 1) / (App.kanjiStudyList.size()));
                    progressLabel.setText((newValue.intValue() + 1) + "/" + App.kanjiStudyList.size());
                })
        );
        //Set up character labels
        charLabel.setText(kanjiDictionary.getByIndex(0).getCharacter());
        onLabel.setText(kanjiDictionary.getByIndex(0).getOnReading());
        kunLabel.setText(kanjiDictionary.getByIndex(0).getKunReading());
        meanLabel.setText(kanjiDictionary.getByIndex(0).getMeaning());

        //Set up vocables
        VocableDictionary kanjiVocab = App.vocableDictionary.getByCharacter(kanjiDictionary.getByIndex(0).getCharacter());
        startedGrid = createStartedGridPane(kanjiVocab);
        if (startedGrid != null) {
            theBox.getChildren().addAll(startedGrid);
        }
        unstartedGrid = createUnstartedGrid(kanjiVocab);
        if (unstartedGrid != null) {
            theBox.getChildren().addAll(unstartedGrid);
        }

        //Set up progress bar
        progressBar.progressProperty().set(1.0 / (App.kanjiStudyList.size()));
        progressLabel.setText(1 + "/" + App.kanjiStudyList.size());
    }

    @FXML
    private void nextButtonAction(ActionEvent actionEvent) throws IOException {
        if (currentIndex.get() < kanjiDictionary.size() - 1) {
            currentIndex.set(currentIndex.get() + 1);
        }
        else {
            showFinishDialog();
        }
    }
    @FXML
    private void previousButtonAction(ActionEvent actionEvent) {
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
        }
    }
    @FXML
    private void xButtonAction(ActionEvent actionEvent) throws IOException {
        showFinishDialog();
    }
    private GridPane createUnstartedGrid(VocableDictionary kanjiVocab) {
        GridPane vGrid = new GridPane();
        
        //coordinates
        int x = 0;
        int y = 0;
        for (Vocable vocable : kanjiVocab) {

            CheckBox vCheckBox = new CheckBox();
            vCheckBox.setStyle("-fx-font-size: 16;");
            
            Label vLabel = new Label(vocable.getForm() + " - " + vocable.getReading()
                    + " - " + vocable.getMeaning());
            vLabel.setWrapText(true);
            vLabel.setStyle("-fx-font-size: 16; -fx-label-padding: 0 5 0 5");

            HBox smallBox = new HBox();
            smallBox.getChildren().addAll(vCheckBox, vLabel);

            //if the vocable isn't started
            if (vocable.getLevel() == 0) {
                //If the vocable was selected before set it to selected
                if (startList.contains(vocable)) {
                    vCheckBox.setSelected(true);
                }

                //Listener gives functionality to selecting
                vCheckBox.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
                    if (newValue) { //if select
                        startList.add(vocable);
                    } else { //if unselect
                        startList.remove(vocable);
                    }
                }));

                vGrid.add(smallBox, x, y);
                if (x == 0) {
                    x++;
                } else {
                    x = 0;
                    y++;
                }
            }
        }
        if (x == 0 && y == 0) { //if there are no unstarted vocables
            return null;
        }

        //Layout stuff for the GridPane
        vGrid.setPrefWidth(theBox.getWidth());
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        vGrid.getColumnConstraints().add(col1);
        
        vGrid.setVgap(5);
        vGrid.setHgap(5);
        
        return vGrid;
    }

    private GridPane createStartedGridPane(VocableDictionary kanjiVocab) {
        GridPane vGrid = new GridPane();

        //coordinates
        int x = 0;
        int y = 0;        
        for (Vocable vocable : kanjiVocab) {
            CheckBox vCheckBox = new CheckBox();
            vCheckBox.setStyle("-fx-font-size: 16;");

            Label vLabel = new Label(vocable.getForm() + " - " + vocable.getReading()
                    + " - " + vocable.getMeaning() + " [Lvl " + vocable.getLevel() + "]");
            vLabel.setWrapText(true);
            vLabel.setStyle("-fx-font-size: 16; -fx-font-size: 16; -fx-label-padding: 0 5 0 5");
            vLabel.setDisable(true);

            HBox smallBox = new HBox();
            smallBox.getChildren().addAll(vCheckBox, vLabel);

            //if the vocable is started                        
            if (vocable.getLevel() != 0) {
                vCheckBox.selectedProperty().setValue(true);
                vCheckBox.disableProperty().setValue(true);
                vGrid.add(smallBox, x, y);
                if (x == 0) {
                    x++;
                } else {
                    x = 0;
                    y++;
                }
            }
        }
        if (x == 0 && y == 0) { //if there are no started vocables
            return null;
        }
        //Layout stuff for the GridPane
        vGrid.setPrefWidth(theBox.getWidth());

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        vGrid.getColumnConstraints().add(col1);
        
        vGrid.setVgap(5);
        vGrid.setHgap(5);

        return vGrid;
    }
    private void showFinishDialog() throws IOException {
        //At the end of the dictionary / when exiting with xButton
        String[] choices;
        Alert finishDialog = new Alert(Alert.AlertType.CONFIRMATION);
        finishDialog.setTitle("Finishing");
        finishDialog.setContentText("Do you want to schedule the vocables you selected for study?");
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        finishDialog.getButtonTypes().setAll(yesButton, noButton, cancelButton);
        Optional<ButtonType> result = finishDialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                for (Vocable v : startList) {
                    v.setLevel(1);
                    v.setDue(LocalDate.now());
                }
                App.vocableDictionary.save(App.settings.getVocableDictionaryFilePath());
                Parent parent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) theBox.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
            else if (result.get().getButtonData().equals(ButtonBar.ButtonData.NO)) {
                Parent parent = FXMLLoader.load(App.class.getResource("fxml/menu/mainMenu.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) theBox.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        }
    }
}