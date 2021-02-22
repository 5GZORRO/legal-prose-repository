package eu._5gzorro.legalproserepository.controller.v1.request.validator;

import eu._5gzorro.legalproserepository.controller.v1.request.constraint.ValidAccordTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LegalProseTemplateFileValidator implements ConstraintValidator<ValidAccordTemplate, MultipartFile> {
    @Override
    public void initialize(ValidAccordTemplate constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if(file.isEmpty())
            return false;

        if(!file.getOriginalFilename().endsWith("cta"))
            return false;

        return fileFormatIsValid(file);
    }

    private boolean fileFormatIsValid(MultipartFile file) {
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {

            ZipEntry ze;
            boolean hasPackageJson = false;
            boolean hasLogicDir = false;
            boolean hasTextDir = false;
            boolean hasModelDir = false;

            while ((ze = zis.getNextEntry()) != null) {

                switch(ze.getName()) {
                    case "package.json":
                        hasPackageJson = true;
                        break;
                    case "text/":
                        hasTextDir = true;
                        break;
                    case "model/":
                        hasModelDir = true;
                        break;
                    case "logic/":
                        hasLogicDir = true;
                }
            }

            return hasPackageJson && hasTextDir && hasModelDir && hasLogicDir;
        }
        catch(IOException ex) {
            return false;
        }
    }
}
