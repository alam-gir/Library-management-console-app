package controller;

import controller.feature.BookListFeature;

public class BookManagementController {

    public void start() {
        new BookListFeature().start();
    }
}
