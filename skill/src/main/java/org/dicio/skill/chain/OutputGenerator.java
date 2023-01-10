package org.dicio.skill.chain;

import androidx.annotation.CallSuper;

import org.dicio.skill.Skill;
import org.dicio.skill.SkillComponent;
import org.dicio.skill.util.CleanableUp;

import java.util.Collections;
import java.util.List;

/**
 * Hosts platform-specific code to produce platform-specific output based on the data obtained from
 * the previous step. Do here all platform-specific things (e.g file accessing,
 * preference handling, etc.).
 * @param <FromType> the type of the data generated by the previous step
 */
public abstract class OutputGenerator<FromType> extends SkillComponent implements CleanableUp {

    // no next skills by default
    private List<Skill> nextSkills = Collections.emptyList();

    /**
     * Generates output to speak or to show to the user based on the data calculated in the previous
     * steps. Do here calculations that require access to platform-specific APIs, such as looking
     * for an app on the user's device.
     *
     * @param data the data to use to generate output, from the previous step
     */
    public abstract void generate(FromType data);

    /**
     * @return a list of skills to use for the next user input. This is needed to allow providing a
     *         stateful interaction with a set of skills. If the list is empty, the current stateful
     *         conversation is interrupted. This function will be called only once, after {@link
     *         #generate(Object)}, so that the calculated data can be used to choose what to do.
     *         There is no need to call {@link Skill#setContext(org.dicio.skill.SkillContext)} and
     *         {@link Skill#setSkillInfo(org.dicio.skill.SkillInfo)} on the returned skills, as that
     *         has to be done by the caller. By default this method returns the last list fed to
     *         {@link #setNextSkills(List)} during the last {@link #generate(Object)} phase (and
     *         then resets that list to an empty list), or an empty list if no list was fed.
     */
    public List<Skill> nextSkills() {
        final List<Skill> skills = nextSkills;
        nextSkills = Collections.emptyList();
        return skills;
    }

    /**
     * @param skills the list of skills to return on the next call to {@link #nextSkills()}. Clears
     *               any previously set list of skills. There is no need to call {@link
     *               Skill#setContext(org.dicio.skill.SkillContext)} and {@link
     *               Skill#setSkillInfo(org.dicio.skill.SkillInfo)} on the skills, as that has to be
     *               done by the caller of {@link #nextSkills()}.
     */
    protected void setNextSkills(final List<Skill> skills) {
        nextSkills = skills;
    }

    /**
     * Resets the last list of next skills passed to {@link #setNextSkills(List)} to an empty list.
     * Override to stop anything this object is doing, dispose disposables, detach listeners, set
     * references to external objects to {@code null}, release resources, etc... Remember to call
     * super if you override.
     */
    @Override
    @CallSuper
    public void cleanup() {
        nextSkills = Collections.emptyList();
    }
}
