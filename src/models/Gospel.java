package models;

import java.time.LocalDateTime;

public class Gospel extends Program {

    private Denomination Denomination;

    public Gospel() {
        ProgramColor = ProgramColor.BLUE;
    }

    public Gospel(models.Denomination denomination) {
        Denomination = denomination;
        ProgramColor = ProgramColor.BLUE;
    }

    public Gospel(String programType, String channelName, String title, String image, String source, String shortDescription, double length, double duration, boolean closedCaption, models.ProgramPhase programPhase, models.ProgramStatus programStatus, models.ProgramColor programColor, LocalDateTime programAirDateTime, models.Denomination denomination) {
        super(programType, channelName, title, image, source, shortDescription, length, duration, closedCaption, programPhase, programStatus, programColor, programAirDateTime);
        super.setProgramColor(ProgramColor.BLUE);
        Denomination = denomination;
    }

    public models.Denomination getDenomination() {
        return Denomination;
    }

    public void setDenomination(models.Denomination denomination) {
        Denomination = denomination;
    }
}
