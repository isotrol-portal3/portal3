/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.isotrol.impe3.samples.randomnr;


import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.core.ConstantRAMRepository;
import com.isotrol.impe3.nr.core.DocumentBuilder;
import com.isotrol.impe3.support.nr.ForwardingNodeRepository;


/**
 * Simple random node repostory implementation.
 * @author Andres Rodriguez
 */
public class RandomNodeRepository extends ForwardingNodeRepository implements InitializingBean {
	private static final String[] TEXTS = {
		"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed nec fermentum augue. Proin a enim ut dolor "
			+ "faucibus lacinia a non urna. Nunc at turpis ultrices ligula pretium porttitor a eu est. Integer sit amet "
			+ "turpis risus. Nullam elementum dictum porta. Duis sed risus eu leo commodo lacinia. Integer mollis "
			+ "fringilla eleifend. Nullam sollicitudin pellentesque mattis. Vivamus nec metus urna. Maecenas ac mattis "
			+ "massa. Cras ut metus sit amet nisi pulvinar tincidunt dignissim ac velit. Integer consectetur dolor felis. "
			+ "Quisque lorem tellus, venenatis sit amet commodo quis, congue nec nibh. Mauris non dui nisi, non luctus "
			+ "diam. Duis pharetra erat iaculis nunc aliquam eu vestibulum sapien mollis. Morbi pharetra condimentum "
			+ "libero at dictum. Integer mattis bibendum lacinia. Integer tristique, nunc eget tincidunt dictum, leo "
			+ "metus condimentum mauris, quis laoreet eros sapien id tortor. ",
		"Praesent lacinia lectus laoreet magna dictum interdum. Donec ullamcorper, velit rhoncus placerat "
			+ "sollicitudin, sapien mauris dictum leo, id viverra urna sapien a nibh. Donec imperdiet tristique elit "
			+ "non posuere. Nullam congue egestas dui, eget fringilla mi aliquam nec. Nulla in nunc lorem, at interdum "
			+ "tortor. Donec porta egestas arcu, non porttitor nibh congue in. Integer vel turpis id orci fermentum "
			+ "tristique a et erat. Quisque tristique egestas ipsum vitae tempus. Vestibulum vel ante justo. Phasellus "
			+ "tempus felis orci, quis consectetur sapien. Nulla vel eros orci, ut vestibulum leo. Vestibulum nulla "
			+ "mauris, tempus quis dignissim vitae, bibendum eu est. Donec dolor nisl, rhoncus nec suscipit vitae, "
			+ "viverra eget mi. Praesent vestibulum convallis elementum. Morbi sagittis sollicitudin placerat. In massa "
			+ "eros, egestas sit amet vestibulum vel, lobortis ut sem. ",
		"Quisque blandit, ligula nec euismod ultricies, enim magna volutpat velit, vel consequat urna risus ac "
			+ "eros. Cras eu turpis sapien. Phasellus sed diam mauris. Nullam condimentum odio vitae arcu pharetra "
			+ "pulvinar. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. "
			+ "Phasellus sed metus in massa blandit tempus volutpat in lacus. Nam massa est, laoreet quis dapibus eu, "
			+ "blandit a dolor. Maecenas felis ante, luctus non adipiscing vitae, sagittis eget eros. Aenean eleifend "
			+ "congue ipsum at gravida. Praesent pharetra purus in lacus hendrerit gravida. Vestibulum ante ipsum primis "
			+ "in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla in nunc vel erat bibendum tempor id "
			+ "id tortor. ",
		"Vivamus orci erat, aliquam a facilisis eu, vehicula a neque. Sed ligula est, pretium non bibendum nec, "
			+ "accumsan sed mi. Aenean egestas elit sed risus blandit vitae pellentesque est rutrum. Donec massa nisi, "
			+ "faucibus et semper eget, luctus varius est. Sed sed dui diam, et posuere risus. Fusce velit odio, "
			+ "vulputate non laoreet vel, tincidunt eu leo. Mauris quis metus quis nibh facilisis luctus. Vestibulum "
			+ "ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Cras fermentum diam at tellus "
			+ "auctor venenatis sit amet et urna. Duis non purus ut arcu auctor sodales vel eu metus. Curabitur nec purus "
			+ "ut lorem interdum fringilla. Vivamus arcu nulla, fermentum sit amet ullamcorper in, venenatis in quam. "
			+ "Pellentesque facilisis dictum ante nec porta. Nunc blandit, sapien ac pretium facilisis, quam nibh "
			+ "facilisis urna, sed sollicitudin quam nisl eget mauris. Vivamus eu sem rhoncus elit sagittis pellentesque.",
		"Donec semper rutrum neque sit amet volutpat. Phasellus viverra vestibulum vulputate. Nullam varius augue "
			+ "eget leo vulputate in pellentesque arcu gravida. Donec auctor odio id purus imperdiet laoreet. Etiam "
			+ "tortor mauris, tincidunt at consequat id, vehicula vel augue. Vivamus fringilla facilisis augue. Nunc "
			+ "luctus, ante eu varius feugiat, nisl ipsum facilisis risus, non tincidunt risus sem eget diam. Nulla eu "
			+ "venenatis mauris. In suscipit quam nec leo ornare at rhoncus nibh rhoncus. Vivamus et diam non lacus "
			+ "auctor placerat. In sit amet risus velit, et lobortis nibh. Aliquam vehicula molestie erat, at lacinia "
			+ "turpis convallis vitae. Morbi lobortis leo a nunc pulvinar in posuere massa placerat. Suspendisse "
			+ "fringilla, dolor consectetur eleifend mattis, elit mauris auctor velit, quis mollis nisl est a sem.",
		"Aliquam imperdiet nisi eget nisl ultricies sollicitudin eu eu mauris. Vestibulum lobortis, ligula at malesuada tempor, magna nunc ornare turpis, a fermentum eros velit dignissim erat. Nullam et mi libero. Duis viverra mauris ultricies lacus fermentum non dapibus nulla aliquet. Duis et sem quis arcu adipiscing malesuada quis at erat. Donec nulla ipsum, sodales at accumsan vitae, sollicitudin a elit. Cras quis lorem a ligula elementum auctor. Mauris ut sem neque. Suspendisse fermentum urna in mauris aliquam sit amet iaculis leo dapibus. In at nisi vitae quam volutpat accumsan. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean nibh nisi, pharetra ut elementum in, tempor vitae lorem. Phasellus eu porttitor justo. Curabitur ipsum ligula, placerat ut imperdiet nec, posuere sed nisi. Suspendisse sapien purus, rhoncus quis ornare sed, lacinia sit amet dolor. Cras a ipsum nulla, id ultrices sem. Curabitur vitae mauris massa, non semper mi. Quisque rhoncus eros id orci sollicitudin elementum. Mauris consequat dolor sed lorem lacinia rutrum. Aliquam interdum, massa fringilla consequat sagittis, sapien magna ultricies eros, ac scelerisque massa ante ut urna. ",
		"Duis sed leo eros, ultrices imperdiet risus. Nunc quis adipiscing nisl. Donec vel diam mauris, ac tincidunt tellus. Sed elementum rutrum augue, ac commodo turpis vestibulum et. Integer consectetur venenatis est sed hendrerit. Integer laoreet est eu est eleifend sit amet sagittis leo aliquet. Nunc eu nisi risus, malesuada congue massa. Nam nec quam est. Nunc facilisis accumsan sem, eget viverra tellus tempus eu. Sed dapibus pulvinar risus at ullamcorper. Maecenas aliquam tempus cursus. Nullam vitae orci eu nunc scelerisque malesuada. Nulla consectetur, felis nec congue vulputate, erat dui adipiscing leo, quis consectetur metus ipsum id eros. Phasellus eget erat augue, a mollis dui. Donec quis laoreet lorem. Donec tempor nulla sem. Morbi ornare suscipit viverra. Nulla libero lectus, varius et mattis et, ultrices eu orci. Donec eleifend dui non neque iaculis eu tristique lacus scelerisque. Donec convallis, justo ut lobortis placerat, metus augue auctor metus, et malesuada nunc dolor sit amet magna.",
		"Donec ac odio nec erat sodales ultricies. Nunc nec neque id tellus rhoncus venenatis. Suspendisse laoreet pharetra purus, vehicula faucibus quam dignissim id. Donec mattis ultrices pellentesque. Pellentesque a eros sem, ut hendrerit libero. Duis varius dictum dapibus. Proin id sapien nec tortor porttitor suscipit vel a leo. Nulla facilisi. Nullam quis turpis eros, sit amet vestibulum leo. Duis et aliquet neque. Pellentesque et vestibulum est. In nisi nunc, egestas quis pulvinar non, bibendum eu dui.",
		"Donec egestas, diam eu fringilla blandit, ipsum ipsum consequat tellus, ultricies rhoncus neque lectus vel diam. Vestibulum hendrerit, lacus vel interdum ornare, sem ipsum vestibulum quam, at scelerisque est mi eget neque. Ut id interdum odio. Sed sodales elit in neque hendrerit non varius nisl scelerisque. Curabitur pulvinar, arcu aliquet congue viverra, lacus mauris scelerisque mauris, sed dapibus justo velit ut arcu. Vestibulum sit amet sem neque. Quisque feugiat scelerisque nunc vitae auctor. Quisque rhoncus sem eu urna cursus rutrum. Nam congue luctus laoreet. Sed sapien lectus, molestie et mattis eget, sollicitudin non nisi. Mauris vulputate tellus quis nulla vestibulum id pellentesque nibh interdum. Sed tincidunt lacinia imperdiet. Nullam odio lorem, luctus hendrerit suscipit quis, commodo vel tellus. Nunc urna neque, mollis eu varius ut, convallis eu tellus. Morbi tellus sapien, facilisis at mollis ut, facilisis id dui. Curabitur lacus turpis, interdum sed pretium vel, accumsan sit amet mi.",
		"Etiam vehicula aliquam metus eu gravida. Duis auctor blandit neque, ut varius erat malesuada sed. Aliquam erat volutpat. Donec gravida augue sit amet lacus volutpat mattis. Aliquam et ligula in purus semper dignissim. Pellentesque porttitor justo in justo varius in feugiat tortor facilisis. In aliquet sapien iaculis tellus sagittis sed posuere felis condimentum. Aliquam dolor ante, adipiscing ac pellentesque sed, fermentum a metus. Fusce id neque sed lectus vehicula iaculis at a enim. Donec et nisl in justo gravida faucibus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Cras arcu metus, fringilla sodales ullamcorper ac, fringilla id nisl. Nullam non lacus a mi tincidunt placerat."};

	private RandomConfig config;
	private NodeRepository repository;
	private ContentTypes contentTypes;
	private Categories categories;

	public RandomNodeRepository() {
	}

	@Override
	protected NodeRepository delegate() {
		return repository;
	}

	public void setConfig(RandomConfig config) {
		this.config = config;
	}

	public void setContentTypes(ContentTypes contentTypes) {
		this.contentTypes = contentTypes;
	}

	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	public void afterPropertiesSet() throws Exception {
		int n = 100;
		if (config != null) {
			Integer s = config.number();
			if (s != null && s > 0) {
				n = s;
			}
		}
		// Content type
		final UUID contentType = contentTypes.isEmpty() ? UUID.randomUUID() : contentTypes.values().iterator().next()
			.getId();
		// Categories
		final List<UUID> list = Lists.newLinkedList((Sets.difference(categories.keySet(), ImmutableSet.of(categories
			.getRoot().getId()))));
		final SecureRandom random = new SecureRandom();
		// Generate documents
		final ConstantRAMRepository.Builder repoBuilder = ConstantRAMRepository.builder();
		for (int i = 1; i <= n; i++) {
			final String text = TEXTS[random.nextInt(TEXTS.length)];
			final DocumentBuilder b = repoBuilder.newTestBuilder(contentType);
			b.setText(text);
			try {
				b.setBytes(text.getBytes("UTF-8"), true);
			} catch (UnsupportedEncodingException e) {
				// nothing
			}
			if (list != null && !list.isEmpty()) {
				b.addCategory(list.get(random.nextInt(list.size())));
			}
			repoBuilder.add(b);
		}
		repository = repoBuilder.build().getRepository();
	}

}
