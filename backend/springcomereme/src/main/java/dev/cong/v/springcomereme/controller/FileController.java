package dev.cong.v.springcomereme.controller;


import dev.cong.v.springcomereme.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class FileController {

    private  final FileService fileService;

    @PostMapping
    public ResponseEntity<?> upload(MultipartFile file){

        return  fileService.upload(file);
    }

    @PostMapping("/multiple")
    public  ResponseEntity<?> uploadMultiple(@RequestParam("files")  List<MultipartFile> multipartFiles){
        return  fileService.uploadMultiple(multipartFiles);
    }


}
