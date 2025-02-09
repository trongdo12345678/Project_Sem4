package com.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.models.BannedKeyword;
import com.utils.Views;

public class BannedKeyword_mapper implements RowMapper<BannedKeyword> {
	@Override
	public BannedKeyword mapRow(ResultSet rs, int rowNum) throws SQLException {
		BannedKeyword bannedKeyword = new BannedKeyword();
		bannedKeyword.setId(rs.getLong(Views.COL_BANNED_KEYWORD_ID));
		bannedKeyword.setKeyword(rs.getString(Views.COL_BANNED_KEYWORD));
		bannedKeyword.setActive(rs.getBoolean(Views.COL_BANNED_KEYWORD_IS_ACTIVE));

		Date createdDate = rs.getDate(Views.COL_BANNED_KEYWORD_CREATED_AT);
		if (createdDate != null) {
			bannedKeyword.setCreatedAt(createdDate.toLocalDate());
		}

		Date updatedDate = rs.getDate(Views.COL_BANNED_KEYWORD_UPDATED_AT);
		if (updatedDate != null) {
			bannedKeyword.setUpdatedAt(updatedDate.toLocalDate());
		}

		return bannedKeyword;
	}
}
