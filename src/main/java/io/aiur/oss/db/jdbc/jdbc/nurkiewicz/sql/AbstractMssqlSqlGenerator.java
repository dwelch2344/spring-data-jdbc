package io.aiur.oss.db.jdbc.jdbc.nurkiewicz.sql;

import org.springframework.data.domain.Pageable;

/**
 * Author: tom
 */
abstract class AbstractMssqlSqlGenerator extends SqlGenerator {
	public AbstractMssqlSqlGenerator() {
	}

	public AbstractMssqlSqlGenerator(String allColumnsClause) {
		super(allColumnsClause);
	}

	@Override
	public String limitClause(Pageable page) {
		return "";
	}
}
