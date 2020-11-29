package controllers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import helpers.DatabaseHelper;
import interfaces.IProgram;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import models.*;
import org.controlsfx.control.Rating;
import tornadofx.control.DateTimePicker;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProgramModificationLayoutController  implements Initializable {

    @FXML
    private TextField title = new TextField();

    @FXML
    private ComboBox<ProgramType> programType = new ComboBox<>();

    @FXML
    private ComboBox<String> channel = new ComboBox<>();

    @FXML
    private DateTimePicker airDate = new DateTimePicker();

    @FXML
    private TextField previewImage = new TextField();

    @FXML
    private TextField programSource = new TextField();

    @FXML
    private TextField length = new TextField();

    @FXML
    private TextField duration = new TextField();

    @FXML
    private ComboBox<ProgramPhase> programPhase = new ComboBox<>();

    @FXML
    private CheckBox closedCaption = new CheckBox();

    @FXML
    private TextField actorFname = new TextField();

    @FXML
    private TextField actorLname = new TextField();

    @FXML
    private TextField actorAge = new TextField();

    @FXML
    private DateTimePicker grammyDate = new DateTimePicker();

    @FXML
    private Button addActor = new Button();

    @FXML
    private Rating movieRating = new Rating();

    @FXML
    private DateTimePicker releaseDate = new DateTimePicker();

    @FXML
    private ComboBox<Denomination> denomination = new ComboBox<>();

    @FXML
    private TextField rangeMin = new TextField();

    @FXML
    private TextField rangeMax = new TextField();

    @FXML
    private Rating weatherRating = new Rating();

    @FXML
    private Button saveProgram = new Button();

    @FXML
    private Button deleteProgram = new Button();

    @FXML
    private Text errorText = new Text();

    @FXML
    private TextArea shortDesc = new TextArea();

    private IProgram program = ProgramsLayoutController.currentlyModifiedProgram == null  ? new Program() : ProgramsLayoutController.currentlyModifiedProgram;

    private List<Actor> actors = program.getActors() == null ? new ArrayList<>() : program.getActors();

    private Actor tempActor = new Actor();

    private Range tempAgeRange = program.getAgeRange() == null ? new Range() : program.getAgeRange();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupComboBoxes();

        configureSaveProgram();

        configureRemoveProgram();

        setProgramValues();

        configureActorInsert();


    }

    private void setupComboBoxes(){

        programType.getItems().addAll(ProgramType.values());

        channel.getItems().addAll(DatabaseHelper.getChannels());

        programPhase.getItems().addAll(ProgramPhase.values());

        denomination.getItems().addAll(Denomination.values());

        channel.onActionProperty().set(e -> {
            program.setChannelName(channel.getValue());
        });

        programType.setOnAction(e -> {

            program.setProgramType(programType.getValue().name());

            program = DatabaseHelper.convertToSpecifiedType(program,program);

            System.out.println(DatabaseHelper.db.toJson(program));
        });

        programPhase.setOnAction(e -> {
            program.setProgramPhase(programPhase.getValue());
        });

        denomination.setOnAction(e -> {
            program.setDenomination(denomination.getValue());
        });
    }

    private void setupModifiedProgramValues(){

        if(program != null){

        }
    }

    private void setProgramValues(){
        title.textProperty().addListener((c,o,n) -> {
            program.setTitle(n);
            System.out.println(DatabaseHelper.db.toJson(program));
        });

        airDate.dateTimeValueProperty().addListener((c,o,n) -> {
            program.setProgramAirDateTime(n);
            System.out.println(DatabaseHelper.db.toJson(program));
        });

        previewImage.textProperty().addListener((c,o,n) -> {
            program.setImage(n);
            System.out.println(DatabaseHelper.db.toJson(program));
        });

        programSource.textProperty().addListener((c,o,n) -> {
            program.setSource(n);
            System.out.println(DatabaseHelper.db.toJson(program));
        });

        length.textProperty().addListener((c,o,n) -> {
            try{
                program.setLength(Double.parseDouble(n));
                System.out.println(DatabaseHelper.db.toJson(program));
                errorText.setText("");
            }catch (Exception e){
                errorText.setText("Please set a valid number for length.");
            }
        });

        duration.textProperty().addListener((c,o,n) -> {
            try{
                program.setDuration(Double.parseDouble(n));
                System.out.println(DatabaseHelper.db.toJson(program));
                errorText.setText("");
            }catch (Exception e){
                errorText.setText("Please set a valid number for duration.");
            }
        });

        closedCaption.selectedProperty().addListener((c,o,n) -> {
            program.setClosedCaption(n);
            System.out.println(DatabaseHelper.db.toJson(program));
        });

        shortDesc.textProperty().addListener((c,o,n) -> {
            program.setShortDescription(n);
            System.out.println(DatabaseHelper.db.toJson(program));
        });

        actorFname.textProperty().addListener((c,o,n) -> {
            tempActor.setFirstName(n);
        });

        actorLname.textProperty().addListener((c,o,n) -> {
            tempActor.setLastName(n);
        });

        actorAge.textProperty().addListener((c,o,n) -> {
            try{
                tempActor.setAge(Integer.parseInt(n));
                errorText.setText("");
            }catch (Exception e){
                errorText.setText("Please set a valid number for actor's age.");
            }
        });

        grammyDate.dateTimeValueProperty().addListener((c,o,n) -> {
            tempActor.setLastGrammyDate(n.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        });

        rangeMin.textProperty().addListener((c,o,n) -> {
            try{
                tempAgeRange.setMin(Integer.parseInt(n));
                program.setAgeRange(tempAgeRange);
                errorText.setText("");
            }catch (Exception e){
                errorText.setText("Please set a valid number for min age.");
            }
        });

        rangeMax.textProperty().addListener((c,o,n) -> {
            try{
                tempAgeRange.setMax(Integer.parseInt(n));
                program.setAgeRange(tempAgeRange);
                errorText.setText("");
                System.out.println(DatabaseHelper.db.toJson(program));
            }catch (Exception e){
                errorText.setText("Please set a valid number for max age.");
            }
        });
    }

    private void configureSaveProgram(){
        saveProgram.setOnMouseClicked(e -> {
            if(e.getButton().equals(MouseButton.PRIMARY)){
                try{
                    if(program != null){
                        DatabaseHelper.addOrUpdateProgram(program);
                        errorText.setText("Program Added Successfully!");
                    }else {
                        errorText.setText("Please set up the necessary values for this program.");
                    }
                }catch (Exception ex){
                    errorText.setText(ex.getMessage());
                }
            }
        });
    }

    private void configureRemoveProgram(){
        deleteProgram.setOnMouseClicked(e -> {
            if(e.getButton().equals(MouseButton.PRIMARY)){
                try{
                    if(program != null){
                        DatabaseHelper.removeProgram(program);
                        errorText.setText("Program Removed Successfully!");
                    }else {
                        errorText.setText("You cannot remove an empty program.");
                    }
                }catch (Exception ex){
                    errorText.setText(ex.getMessage());
                }
            }
        });
    }

    private void configureActorInsert(){

        addActor.setOnMouseClicked(e -> {

            if(tempActor.getFirstName() == null || tempActor.getLastName() == null || tempActor.getAge() <= 0){
                errorText.setText("Please set the necessary fields for the actor.");
                return;
            }

            try{
                List<Actor> existInList = actors.stream().filter(a -> a.getFirstName().toLowerCase().equals(tempActor.getFirstName().toLowerCase())
                        && a.getLastName().toLowerCase().equals(tempActor.getLastName().toLowerCase())).collect(Collectors.toList());

                if(existInList.size() > 0){
                    errorText.setText("Actor already exist.");
                    return;
                }
            }catch (Exception ex){}

            Actor actor = DatabaseHelper.db.fromJson(DatabaseHelper.db.toJson(tempActor), new TypeToken<>(){});
            tempActor = new Actor();
            actors.add(actor);
            program.setActors(actors);
            actorFname.setText("");
            actorLname.setText("");
            actorAge.setText("0");
            errorText.setText("");
            grammyDate.setDateTimeValue(LocalDateTime.now());
            System.out.println(DatabaseHelper.db.toJson(program));
        });

    }
}
