/**
 * Copyright (c) 2011-2019, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.plugin.activerecord.generator;

import javax.sql.DataSource;

public class LocalGenerator extends Generator {

	public LocalGenerator(DataSource dataSource, BaseModelGenerator baseModelGenerator) {
		super(dataSource, baseModelGenerator);
	}
	
	/**
	 * 构造 Generator，生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
	 * @param dataSource 数据源
	 * @param baseModelPackageName base model 包名
	 * @param baseModelOutputDir base mode 输出目录
	 * @param modelPackageName model 包名
	 * @param modelOutputDir model 输出目录
	 */
	public LocalGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir) {
		super(dataSource, baseModelPackageName, baseModelOutputDir,modelPackageName,modelOutputDir);
	}
	
	/**
	 * 构造 Generator，只生成 baseModel
	 * @param dataSource 数据源
	 * @param baseModelPackageName base model 包名
	 * @param baseModelOutputDir base mode 输出目录
	 */
	public LocalGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir) {
		super(dataSource, baseModelPackageName, baseModelOutputDir);
	}
	
	/**
	 * 使用指定 BaseModelGenerator、ModelGenerator 构造 Generator 
	 * 生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
	 */
	public LocalGenerator(DataSource dataSource, BaseModelGenerator baseModelGenerator, ModelGenerator modelGenerator) {
		super(dataSource, baseModelGenerator, modelGenerator);
	}
	
	/**
	 * 添加需要处理的数据表
	 */
	public void addIncludedTable(String... includedTables) {
		metaBuilder.addIncludedTable(includedTables);
	}
}



