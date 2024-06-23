package com.microsoft.aspire.storage.explorer;

import com.microsoft.aspire.storage.explorer.service.StorageItem;
import com.microsoft.aspire.storage.explorer.service.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Controller
public class StorageServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceController.class);
    private final StorageService storageService;

    @Value("${services__dateservice__https__0}")
    private String dateServiceEndpoint;

    @Autowired
    public StorageServiceController(final StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(final Model model,
                                    final HttpServletResponse response) {
        model.addAttribute("files", storageService.listAllFiles().collect(Collectors.toList()));
        updateTime(model);
        response.addHeader("Cache-Control", "no-cache");
        return "uploadForm";
    }

    private void updateTime(Model model) {
        LOGGER.info("The datetime service endpoint is " + dateServiceEndpoint);
        String lastUpdated = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            OffsetDateTime time = restTemplate.getForObject(dateServiceEndpoint + "/time", OffsetDateTime.class);
            if (time != null) {
                lastUpdated = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to get time from datetime service", exception);
        }
        model.addAttribute("time", lastUpdated);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable final String filename) {
        final StorageItem storageItem = storageService.getFile(filename);

        final String contentDisposition;
        switch (storageItem.getContentDisplayMode()) {
            default:
            case DOWNLOAD: {
                contentDisposition = "attachment";
                break;
            }
            case MODAL_POPUP:
            case NEW_BROWSER_TAB: {
                contentDisposition = "inline";
            }
        }

        final Resource body = new InputStreamResource(storageItem.getContent(), storageItem.getFileName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(storageItem.getContentType()))
                .body(body);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") final MultipartFile file,
                                   final RedirectAttributes redirectAttributes) {
        boolean success = false;
        try {
            storageService.store(file.getOriginalFilename(), file.getInputStream(), file.getSize());
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("success", success);
        redirectAttributes.addFlashAttribute("message", success ?
                "You successfully uploaded " + file.getOriginalFilename() + "!" :
                "Failed to upload " + file.getOriginalFilename());

        return "redirect:/";
    }

    @GetMapping("/files/delete/{filename}")
    public String deleteFile(@PathVariable final String filename,
                             final RedirectAttributes redirectAttributes) {
        final boolean success = storageService.deleteFile(filename);

        redirectAttributes.addFlashAttribute("success", success);
        redirectAttributes.addFlashAttribute("message", success ?
                "You successfully deleted " + filename + "!" :
                "Failed to delete " + filename + ".");

        return "redirect:/";
    }
}