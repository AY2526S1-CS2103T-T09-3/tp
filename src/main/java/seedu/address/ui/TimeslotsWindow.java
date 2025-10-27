package seedu.address.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seedu.address.model.timeslot.ConsultationTimeslot;
import seedu.address.model.timeslot.Timeslot;

/**
 * Timetable-style window to display merged timeslot ranges as horizontal blocks.
 * The timeline spans from START_HOUR (inclusive) to END_HOUR (exclusive).
 *
 * Each LocalDateTime[] in mergedRanges is interpreted as {start, end}.
 */
public class TimeslotsWindow {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int START_HOUR = 8;
    private static final int END_HOUR = 24;
    private static final int HOURS = END_HOUR - START_HOUR;
    private static final double PIXELS_PER_MINUTE = 1.0; // 60 minutes => 60px per hour (adjust if needed)
    private static final double TIMELINE_WIDTH = HOURS * 60 * PIXELS_PER_MINUTE; // total px width for timeline
    private static final double ROW_HEIGHT = 64;

    // New shared layout constants to ensure exact alignment between header and rows
    private static final double DAY_LABEL_WIDTH = 90; // left column width used by day labels
    private static final double ROW_SPACING = 8; // spacing used between day label and timeline
    private static final double BODY_PADDING = 8; // must match VBox body padding used in renderWeek
    private static final double TOP_CONTAINER_SPACING = 24; // increased vertical gap between header and hours

    // Keep a reference to the most-recently created Stage so callers (e.g. MainWindow) can hide it.
    private static Stage currentStage = null;

    /**
     * Shows the merged timeslot ranges in a new window laid out as a timetable.
     * Each entry in {@code mergedRanges} should be a LocalDateTime[2] array: [start, end].
     * Receives the concrete timeslot list so consultations (with student names) can be rendered specially.
     */
    public static void showMerged(List<LocalDateTime[]> mergedRanges, List<Timeslot> allTimeslots) {
        Stage stage = new Stage();
        // remember the stage so clients can hide it
        currentStage = stage;
        stage.setTitle("My Schedule");
        stage.initModality(Modality.NONE);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label header = new Label("Timetable");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        BorderPane.setAlignment(header, Pos.CENTER);

        // Navigation controls
        Button nextWeekBtn = new Button("Next Week");
        Button prevWeekBtn = new Button("Previous Week");

        // Determine initial week start: prefer current week's Monday, but if the earliest timeslot
        // starts before the current week, show that earlier week so the timeslot is visible.
        LocalDate currentMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // compute earliest date in mergedRanges (if provided) in a style that avoids complex chained calls
        LocalDate earliestDate = null;
        if (mergedRanges != null && !mergedRanges.isEmpty()) {
            earliestDate = mergedRanges.stream()
                    .map(r -> r[0]) // start LocalDateTime
                    .filter(Objects::nonNull)
                    .map(LocalDateTime::toLocalDate)
                    .min(LocalDate::compareTo)
                    .orElse(null);
        }
        final LocalDate[] weekStartRef = new LocalDate[1];
        weekStartRef[0] = (earliestDate != null && earliestDate.isBefore(currentMonday))
                ? earliestDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                : currentMonday;

        // top row: header + spacer + navigation buttons
        HBox topRow = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(ROW_SPACING);
        topRow.getChildren().addAll(header, spacer, prevWeekBtn, nextWeekBtn);

        // timeline width property shared by all timeline panes so they resize together
        DoubleProperty timelineWidth = new SimpleDoubleProperty(TIMELINE_WIDTH);

        // hours header (kept same as existing) - now takes timelineWidth
        HBox hoursHeader = buildHoursHeader(timelineWidth);
        // no padding here — buildHoursHeader now inserts a left placeholder matching the day label column
        // so ticks align exactly with the timelines.

        // render initial week
        renderWeek(root, weekStartRef[0], mergedRanges, allTimeslots, topRow, hoursHeader, timelineWidth);

        // button actions: adjust weekStart and re-render
        nextWeekBtn.setOnAction(e -> {
            if (weekStartRef[0] == null) {
                weekStartRef[0] = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            } else {
                weekStartRef[0] = weekStartRef[0].plusWeeks(1);
            }
            renderWeek(root, weekStartRef[0], mergedRanges, allTimeslots, topRow, hoursHeader, timelineWidth);
        });
        prevWeekBtn.setOnAction(e -> {
            if (weekStartRef[0] == null) {
                weekStartRef[0] = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            } else {
                weekStartRef[0] = weekStartRef[0].minusWeeks(1);
            }
            renderWeek(root, weekStartRef[0], mergedRanges, allTimeslots, topRow, hoursHeader, timelineWidth);
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);

        // allow user to resize the window
        stage.setResizable(true);
        // set sensible minimum size so layout remains usable
        stage.setMinWidth(600);
        stage.setMinHeight(400);

        stage.show();

        // clear currentStage when user closes the window
        stage.setOnHidden(evt -> {
            if (currentStage == stage) {
                currentStage = null;
            }
        });

        // Bind timelineWidth to scene width (minimum remains TIMELINE_WIDTH). Subtract left label area and padding.
        NumberBinding widthBinding = Bindings.createDoubleBinding(() ->
            Math.max(scene.getWidth() - 120, TIMELINE_WIDTH),
            scene.widthProperty());
        timelineWidth.bind(widthBinding);

        // After showing, bind the ScrollPane viewport to the scene size so it grows/shrinks with the window.
        if (root.getCenter() instanceof ScrollPane) {
            ScrollPane sp = (ScrollPane) root.getCenter();
            sp.prefViewportWidthProperty().bind(scene.widthProperty().subtract(120));
            sp.prefViewportHeightProperty().bind(scene.heightProperty().subtract(140));
        }
    }

    /**
     * Hides the currently shown Timeslots window if any.
     */
    public static void hide() {
        if (currentStage != null) {
            try {
                currentStage.hide();
            } finally {
                currentStage = null;
            }
        }
    }

    /**
     * Returns true if a Timeslots window is currently visible.
     */
    public static boolean isShowing() {
        return currentStage != null && currentStage.isShowing();
    }

    // Renders the given week starting at weekStart (Monday) into root using provided topRow and hoursHeader nodes.
    private static void renderWeek(BorderPane root, LocalDate weekStart, List<LocalDateTime[]> ranges,
            List<Timeslot> allTimeslots, HBox topRow, HBox hoursHeader, DoubleProperty timelineWidth) {
        // Defensive fallback: if weekStart is null, use current week's Monday so UI always shows a valid week.
        if (weekStart == null) {
            weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }
        // Body: one row per day with timeline pane for the week starting at weekStart
        VBox body = new VBox(6);
        body.setPadding(new Insets(8));
        for (int i = 0; i < 7; i++) { // 7 days in a week
            LocalDate date = weekStart.plusDays(i);
            HBox row = buildDayRowForDate(date, ranges, allTimeslots, timelineWidth);
            body.getChildren().add(row);
        }

        ScrollPane sp = new ScrollPane(body);
        sp.setFitToWidth(true);
        sp.setPrefViewportWidth(Math.min(TIMELINE_WIDTH + 120, 1000));
        sp.setPrefViewportHeight(ROW_HEIGHT * 5 + 200);

        // place topRow and hoursHeader in a container with larger vertical spacing
        VBox topContainer = new VBox(TOP_CONTAINER_SPACING, topRow, hoursHeader);
        topContainer.setSpacing(TOP_CONTAINER_SPACING);
        root.setTop(topContainer);

        root.setCenter(sp);
    }

    // build the top hours labels and vertical tick lines; timelineWidth drives the timeline Pane width
    private static HBox buildHoursHeader(DoubleProperty timelineWidth) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(0);

        // left placeholder to align with dayLabel (minWidth = DAY_LABEL_WIDTH + ROW_SPACING + BODY_PADDING)
        Region leftGap = new Region();
        leftGap.setMinWidth(DAY_LABEL_WIDTH + ROW_SPACING + BODY_PADDING);
        leftGap.setPrefWidth(DAY_LABEL_WIDTH + ROW_SPACING + BODY_PADDING);
        header.getChildren().add(leftGap);

        Pane timeline = new Pane();
        // bind timeline width to the shared property so it grows/shrinks with the window
        timeline.prefWidthProperty().bind(timelineWidth);
        timeline.setPrefHeight(24);

        for (int h = 0; h <= HOURS; h++) {
            // position ratio relative to base TIMELINE_WIDTH so positions scale
            double baseX = h * 60 * PIXELS_PER_MINUTE;
            double ratio = baseX / TIMELINE_WIDTH; // baseX / TIMELINE_WIDTH in [0,1]
            NumberBinding xBind = timelineWidth.multiply(ratio);

            Line tick = new Line();
            tick.startXProperty().bind(xBind);
            tick.endXProperty().bind(xBind);
            tick.setStartY(0);
            tick.setEndY(24);
            // softer tick color
            tick.setStroke(Color.web("#c5d0da"));
            tick.setStrokeWidth(1);
            timeline.getChildren().add(tick);

            if (h < HOURS) {
                Label hourLabel = new Label(String.format("%02d:00", START_HOUR + h));
                hourLabel.setStyle("-fx-text-fill: #556070; -fx-font-size: 11px;");
                hourLabel.layoutXProperty().bind(xBind.add(4));
                hourLabel.setLayoutY(2);
                timeline.getChildren().add(hourLabel);
            }
        }

        header.getChildren().add(timeline);
        return header;
    }

    // New: buildDayRowForDate renders a row for a given LocalDate (shows label with date)
    private static HBox buildDayRowForDate(LocalDate date, List<LocalDateTime[]> ranges,
            List<Timeslot> allTimeslots, DoubleProperty timelineWidth) {
        HBox row = new HBox();
        row.setSpacing(ROW_SPACING);
        row.setAlignment(Pos.CENTER_LEFT);
        // set the same padding here as used when creating the body so layout offsets match
        row.setPadding(new Insets(0, 0, 0, 0));

        String dayLabelText = String.format("%s %s", date.getDayOfWeek().toString().substring(0, 3),
                date.format(DateTimeFormatter.ofPattern("MM/dd")));
        Label dayLabel = new Label(dayLabelText);
        dayLabel.setMinWidth(DAY_LABEL_WIDTH);
        dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");

        Pane timeline = new Pane();
        // light background and subtle border for low contrast
        timeline.setStyle("-fx-background-color: #fbfcfd; -fx-border-color: #e6eef6; -fx-border-radius: 4;");
        // bind timeline width to the shared property so it scales with the window width
        timeline.prefWidthProperty().bind(timelineWidth);
        timeline.setPrefHeight(ROW_HEIGHT);
        timeline.setOpacity(0.98);

        // add hour divider lines lightly (positions scale with timelineWidth)
        for (int h = 0; h <= HOURS; h++) {
            double baseX = h * 60 * PIXELS_PER_MINUTE;
            double ratio = baseX / TIMELINE_WIDTH;
            NumberBinding xBind = timelineWidth.multiply(ratio);

            Line divider = new Line();
            divider.startXProperty().bind(xBind);
            divider.endXProperty().bind(xBind);
            divider.setStartY(0);
            divider.setEndY(ROW_HEIGHT);
            divider.setStroke(Color.web("#e6eef6"));
            divider.setStrokeWidth(0.8);
            timeline.getChildren().add(divider);
        }

        // Place a block for each visible portion of each timeslot that intersects this date and the displayed window
        if (ranges != null) {
            for (LocalDateTime[] range : ranges) {
                LocalDateTime start = range[0];
                LocalDateTime end = range[1];
                if (start == null || end == null) {
                    continue;
                }

                // Compute the portion of [start,end) that falls on 'date'
                LocalDateTime dayWindowStart = LocalDateTime.of(date, LocalTime.of(START_HOUR, 0));
                LocalDateTime dayWindowEnd = LocalDateTime.of(date.plusDays(1), LocalTime.MIDNIGHT);

                LocalDateTime renderStart = start.isAfter(dayWindowStart) ? start : dayWindowStart;
                LocalDateTime renderEnd = end.isBefore(dayWindowEnd) ? end : dayWindowEnd;

                if (renderStart.isBefore(renderEnd)) {
                    double minutesFromStart = (renderStart.getHour() * 60 + renderStart.getMinute()) - START_HOUR * 60;
                    double durationMinutes = (renderEnd.getHour() * 60 + renderEnd.getMinute())
                            - (renderStart.getHour() * 60 + renderStart.getMinute());

                    // Compute ratios relative to base timeline so they scale with timelineWidth
                    double xRatio = (minutesFromStart * PIXELS_PER_MINUTE) / TIMELINE_WIDTH;
                    double wRatio = (durationMinutes * PIXELS_PER_MINUTE) / TIMELINE_WIDTH;

                    NumberBinding xBind = timelineWidth.multiply(xRatio);
                    NumberBinding wBind = timelineWidth.multiply(wRatio);

                    Rectangle block = new Rectangle();
                    // bind position and size so block scales with timelineWidth
                    block.xProperty().bind(xBind);
                    block.yProperty().set(8);
                    block.widthProperty().bind(wBind);
                    block.setHeight(ROW_HEIGHT - 16);
                    block.setArcWidth(8);
                    block.setArcHeight(8);
                    block.setFill(pickColorForDay(date.getDayOfWeek()));
                    // softer stroke for blocks
                    block.setStroke(Color.web("#d0d7de"));
                    block.setStrokeWidth(1);

                    // Decide whether to hide the generic timeslot label.
                    // We only hide the label if a consultation actually covers the label area at the start
                    // of the timeslot block. This prevents hiding labels when a consultation overlaps later.
                    final int labelSafeMinutes = 15;
                    boolean hideGenericLabel = false;
                    if (allTimeslots != null) {
                        for (Timeslot t2 : allTimeslots) {
                            if (!(t2 instanceof ConsultationTimeslot)) {
                                continue;
                            }
                            LocalDateTime cStart = t2.getStart();
                            LocalDateTime cEnd = t2.getEnd();
                            if (cStart == null || cEnd == null) {
                                continue;
                            }
                            // compute the portion of the consultation that falls on this date window
                            LocalDateTime cRenderStart = cStart.isAfter(dayWindowStart) ? cStart : dayWindowStart;
                            LocalDateTime cRenderEnd = cEnd.isBefore(dayWindowEnd) ? cEnd : dayWindowEnd;
                            // if the consultation visible portion overlaps the timeslot at all
                            if (cRenderStart.isBefore(renderEnd) && cRenderEnd.isAfter(renderStart)) {
                                // hide the generic label only if the consultation covers the label area near the start
                                if (cRenderStart.isBefore(renderStart.plusMinutes(labelSafeMinutes))
                                        && cRenderEnd.isAfter(renderStart)) {
                                    hideGenericLabel = true;
                                    break;
                                }
                            }
                        }
                    }

                    // Only allow line breaks at the time separator " - " so we avoid character-level wrapping.
                    // Show times as two lines with explicit S: (start) and E: (end) prefixes.
                    String displayLabelText = String.format("S: %s\nE: %s",
                            renderStart.format(DateTimeFormatter.ofPattern("HH:mm")),
                            renderEnd.format(DateTimeFormatter.ofPattern("HH:mm")));
                    Label bl = new Label(displayLabelText);

                    bl.setStyle("-fx-text-fill: #1f2a2f; -fx-font-size: 11px;");
                    // bind label position to block.x + 6 so it follows scaling
                    bl.layoutXProperty().bind(xBind.add(6));
                    bl.setLayoutY(8); // vertical placement

                    Tooltip tooltip = new Tooltip(start.format(FORMATTER) + "\n" + end.format(FORMATTER));
                    Tooltip.install(block, tooltip);

                    // Add the block, and add the generic time label only if it's not obscured by a consultation
                    // near the block start.
                    if (!hideGenericLabel) {
                        Tooltip.install(bl, tooltip);
                        timeline.getChildren().addAll(block, bl);
                    } else {
                        timeline.getChildren().add(block);
                    }
                }
            }
        }

        // Render consultations (distinct appearance + student name) from concrete timeslot list.
        if (allTimeslots != null) {
            for (Timeslot t : allTimeslots) {
                if (!(t instanceof ConsultationTimeslot)) {
                    continue;
                }
                ConsultationTimeslot ct = (ConsultationTimeslot) t;
                LocalDateTime start = ct.getStart();
                LocalDateTime end = ct.getEnd();
                if (start == null || end == null) {
                    continue;
                }

                // same intersection logic as above: check if this consultation overlaps the date and timeline window
                LocalDateTime dayWindowStart = LocalDateTime.of(date, LocalTime.of(START_HOUR, 0));
                LocalDateTime dayWindowEnd = LocalDateTime.of(date.plusDays(1), LocalTime.MIDNIGHT);

                LocalDateTime renderStart = start.isAfter(dayWindowStart) ? start : dayWindowStart;
                LocalDateTime renderEnd = end.isBefore(dayWindowEnd) ? end : dayWindowEnd;

                if (renderStart.isBefore(renderEnd)) {
                    double minutesFromStart = (renderStart.getHour() * 60 + renderStart.getMinute()) - START_HOUR * 60;
                    double durationMinutes = (renderEnd.getHour() * 60 + renderEnd.getMinute())
                            - (renderStart.getHour() * 60 + renderStart.getMinute());

                    double xRatio = (minutesFromStart * PIXELS_PER_MINUTE) / TIMELINE_WIDTH;
                    double wRatio = (durationMinutes * PIXELS_PER_MINUTE) / TIMELINE_WIDTH;

                    NumberBinding xBind = timelineWidth.multiply(xRatio);
                    NumberBinding wBind = timelineWidth.multiply(wRatio);

                    // Render consultation as a simple red block; student name shown below the time label.
                    Rectangle consultBlock = new Rectangle();
                    consultBlock.xProperty().bind(xBind);
                    consultBlock.yProperty().set(8); // same vertical placement as other blocks
                    consultBlock.widthProperty().bind(wBind);
                    consultBlock.setHeight(ROW_HEIGHT - 16);
                    consultBlock.setArcWidth(8);
                    consultBlock.setArcHeight(8);
                    // Always red for consultations
                    consultBlock.setFill(Color.web("#d96565ff"));
                    consultBlock.setStroke(Color.web("#c94b4b"));
                    consultBlock.setStrokeWidth(1);

                    // Small icon to the left of the time label
                    Label icon = new Label("\u2605"); // ★
                    icon.setStyle("-fx-text-fill: #FFD23F; -fx-font-size: 14px; -fx-font-weight: bold;");
                    // Place star a small distance from the block start; align vertically with the time label.
                    icon.layoutXProperty().bind(xBind.add(6));
                    icon.setLayoutY(10);

                    // Time label
                    String timeLblText = String.format("S: %s\nE: %s",
                            renderStart.format(DateTimeFormatter.ofPattern("HH:mm")),
                            renderEnd.format(DateTimeFormatter.ofPattern("HH:mm")));
                    Label timeLbl = new Label(timeLblText);

                    timeLbl.setStyle("-fx-text-fill: #000000ff; -fx-font-size: 11px;");
                    timeLbl.layoutXProperty().bind(xBind.add(18)); // icon (8px) + small gap
                    timeLbl.setLayoutY(8);

                    // Student name label placed below the time label
                    String studentText = ct.getStudentName();
                    // allow student name to break at spaces if necessary
                    Label studentLbl = new Label(studentText.contains(" ")
                        ? studentText.replace(" ", "\n")
                        : studentText);
                    studentLbl.setStyle("-fx-text-fill: #000000ff; -fx-font-size: 11px;");
                    studentLbl.layoutXProperty().bind(xBind.add(18));
                    // Keep student label positioned directly below the time label even after the time label wraps.
                    studentLbl.layoutYProperty().bind(timeLbl.layoutYProperty().add(timeLbl.heightProperty()).add(2));

                    String consultTooltip = String.format("Consultation: %s -> %s%nStudent: %s",
                            start.format(FORMATTER),
                            end.format(FORMATTER),
                            ct.getStudentName());
                    Tooltip tooltip = new Tooltip(consultTooltip);
                    Tooltip.install(consultBlock, tooltip);
                    Tooltip.install(timeLbl, tooltip);
                    Tooltip.install(studentLbl, tooltip);
                    Tooltip.install(icon, tooltip);

                    // Add consultation visuals: block, icon, then labels
                    timeline.getChildren().addAll(consultBlock, icon, timeLbl, studentLbl);
                    // Ensure labels and icon are rendered above the block.
                    icon.toFront();
                    timeLbl.toFront();
                    studentLbl.toFront();
                }
            }
        }

        row.getChildren().addAll(dayLabel, timeline);
        return row;
    }

    // pick a pleasant color for the day (cycled)
    private static Color pickColorForDay(DayOfWeek dow) {
        // Pastel/soft palette for reduced contrast and easier viewing
        return switch (dow) {
        case MONDAY -> Color.web("#9ec5ff"); // soft blue
        case TUESDAY -> Color.web("#b8e6d8"); // mint
        case WEDNESDAY -> Color.web("#ffd9a8"); // peach
        case THURSDAY -> Color.web("#d6d5ff"); // lavender
        case FRIDAY -> Color.web("#f6cfe6"); // light pink
        case SATURDAY -> Color.web("#d9f5d7"); // light green
        case SUNDAY -> Color.web("#f7eddc"); // light cream
        default -> Color.web("#cbd5df");
        };
    }


}
