package hr.fer.zemris.java.hw07.shell.nameparsing;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a NameBuilder instance which in usage, finally collects
 * all the NameBuilders instances used in process of generating a new file name.
 * 
 * @author Dinz
 *
 */
public class NameBuilderFinal implements NameBuilder {
	/**
	 * List of NameBuilder instances.
	 */
	List<NameBuilder> nameBuilderlist;

	/**
	 * Constucts a new NameBuilderFinal instance.
	 * 
	 * @param nbList
	 */
	public NameBuilderFinal(List<NameBuilder> nbList) {
		nameBuilderlist = new ArrayList<>(nbList);
	}

	@Override
	public void execute(NameBuilderInfo info) {
		for (NameBuilder nb : nameBuilderlist) {
			nb.execute(info);
		}

	}

}
