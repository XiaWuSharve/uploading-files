package com.example.uploading_files;

import static org.awaitility.Awaitility.given;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.uploading_files.storage.StorageService;

@AutoConfigureMockMvc
@SpringBootTest
class UploadingFilesApplicationTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StorageService storageService;

	@Test
	public void shouldListAllFiles() {
		
	}

}
