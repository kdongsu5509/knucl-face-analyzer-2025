package com.knucl.FaceAnalyze.myException;

public enum ErrorCode {
    NO_FILE_EXTENTION("No file extension found."),
    INVALID_FILE_EXTENTION("Invalid file extension."),
    PUT_OBJECT_EXCEPTION("Exception occurred while putting the object in S3."),
    IO_EXCEPTION_ON_IMAGE_DELETE("I/O Exception occurred while deleting the image."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD("I/O Exception occurred while uploading the image."),
    EMPTY_FILE_EXCEPTION("Empty file exception."),
    URL_GENERATION_EXCEPTION("Exception occurred while generating the URL.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
