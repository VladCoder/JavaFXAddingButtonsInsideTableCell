package ru.vlsoft.table_cells;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.vlsoft.models.Product;
import ru.vlsoft.view_models.ProductListViewModel;

import java.util.Optional;

public class ProductTableCell<T> extends TableCell<T, Product> {

    private Label label;
    private TextField textField;
    private Button buttonChoiceDialog;
    private Button buttonErase;

    public ProductTableCell() {
        label = new Label();
        textField = new TextField();
        buttonChoiceDialog = new Button("..");
        buttonErase = new Button("X");
    }

    @Override
    public void startEdit() {
        super.startEdit();

        setEditableContent();

    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);

        setReadableContent();
    }

    @Override
    public void commitEdit(Product product) {
        super.commitEdit(product);

        setReadableContent();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setReadableContent();
    }

    private void setEditableContent() {

        double width = getWidth();
        double height = getHeight();
        double margin = 4.;

        if (getItem() == null) {
            textField.setText("");
        } else {
            textField.setText(getItem().getName());
        }

        buttonChoiceDialog.setMinWidth(height - margin);
        buttonChoiceDialog.setMaxWidth(height - margin);
        buttonChoiceDialog.setPadding(new Insets(margin, 0, margin, 0));
        buttonChoiceDialog.setOnAction(handler -> System.out.println("asd"));
        buttonChoiceDialog.setOnAction(handler -> {

            Dialog<ButtonType> dialog = new Dialog<>();
            DialogPane pane = dialog.getDialogPane();
            pane.getButtonTypes().addAll(
                    ButtonType.OK,
                    ButtonType.CANCEL);
            pane.setContent(new ProductListViewModel().getSelectionDialogContent());
            pane.setMinWidth(600.);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.orElseThrow().getButtonData() == ButtonBar.ButtonData.OK_DONE) {

                    try {
                        TableView<Product> dialogTableView = (TableView<Product>) ((VBox) pane.contentProperty().getValue()).getChildren().get(0);
                        commitEdit(dialogTableView.getSelectionModel().getSelectedItem());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                } else {
                    cancelEdit();
                }
            }

        });

        buttonErase.setMinWidth(height - margin);
        buttonErase.setMaxWidth(height - margin);
        buttonErase.setPadding(new Insets(margin, 0, margin, 0));
        buttonErase.setOnAction(handler -> {
            setItem(null);
            setText("");
            commitEdit(null);
        });

        HBox box = new HBox();
        box.setMinWidth(width - margin);
        box.setMaxWidth(width - margin);
        box.getChildren().add(textField);
        box.getChildren().add(buttonChoiceDialog);
        box.getChildren().add(buttonErase);

        setGraphic(box);

    }

    private void setReadableContent() {
        if (getItem() == null) {
            setGraphic(null);
        } else {
            label.setText(getItem().getName());
            setGraphic(label);
        }
    }

}
