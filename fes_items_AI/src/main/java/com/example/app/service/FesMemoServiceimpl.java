package com.example.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.FesMemo;
import com.example.app.mapper.FesMemoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FesMemoServiceimpl implements FesMemoService {

	private final FesMemoMapper mapper;
	@Value("${memo.upload-dir}")
	private String uploadDir;

	@Override
	public List<FesMemo> list(Integer userId) {
		return mapper.findByUser(userId);
	}

	@Override
	public FesMemo get(Integer id, Integer userId) {
		return mapper.findByIdAndUser(id, userId);
	}

	@Override
	public void create(FesMemo memo, MultipartFile image, Integer userId) throws IOException {
		memo.setUserId(userId);
		memo.setImagePath(saveImage(image, null));
		mapper.insert(memo);
	}

	@Override
	public void update(FesMemo memo, MultipartFile image, Integer userId) throws IOException {
		FesMemo cur = mapper.findByIdAndUser(memo.getId(), userId);
		if (cur == null)
			return;
		memo.setUserId(userId);
		String path = saveImage(image, cur.getImagePath());
		memo.setImagePath(path != null ? path : cur.getImagePath());
		mapper.update(memo);
	}

	@Override
	public void delete(Integer id, Integer userId) throws IOException {
		FesMemo cur = mapper.findByIdAndUser(id, userId);
		if (cur != null && StringUtils.hasText(cur.getImagePath()))
			deletePhysical(cur.getImagePath());
		mapper.delete(id, userId);
	}

	private String saveImage(MultipartFile file, String oldPath) throws IOException {
		if (file == null || file.isEmpty())
			return null;
		if (file.getContentType() == null || !file.getContentType().matches("image/(png|jpe?g|gif)"))
			throw new IOException("画像は png/jpg/gif のみ対応");

		Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
		Files.createDirectories(base);
		String ext = switch (Objects.requireNonNull(file.getContentType())) {
		case "image/png" -> ".png";
		case "image/gif" -> ".gif";
		default -> ".jpg";
		};
		String name = UUID.randomUUID() + ext;
		Files.copy(file.getInputStream(), base.resolve(name), StandardCopyOption.REPLACE_EXISTING);

		if (StringUtils.hasText(oldPath))
			deletePhysical(oldPath);
		return "/uploads/" + name; // Web公開パス
	}

	private void deletePhysical(String publicPath) throws IOException {
		Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
		Files.deleteIfExists(base.resolve(Paths.get(publicPath).getFileName()));
	}
}
	
	
	

