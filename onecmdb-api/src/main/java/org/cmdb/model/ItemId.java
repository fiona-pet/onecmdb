package org.cmdb.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tom on 2017/8/1.
 */
public class ItemId implements Serializable, Comparable {

    private static final long serialVersionUID = 98836700497597228L;
    private final long longHash;
    private String delegate;

    /**
     * Constructs a new, unique, identifier
     */
    public ItemId() {
        UUID uuid = UUID.randomUUID();
        this.longHash = uuid.getMostSignificantBits() ^
                uuid.getLeastSignificantBits();

        this.delegate = uuid.toString();
    }

    /**
     * Recreate the identifier using the known <em>longHash</em>
     *
     * @param hash
     */
    public ItemId(long hash) {
        this.longHash = hash;
    }

    /**
     * Recreate the identifier using the lexical string representation of item.
     *
     * @param text
     * @see #toString
     */
    public ItemId(final String text) {
        String parseOrig;
        if (text.startsWith("#") && text.length() >= 1) {
            parseOrig = text.substring(1);
        } else {
            parseOrig = text;
        }
        if (parseOrig.indexOf('?') != -1) {
            parseOrig = parseOrig.substring(0, parseOrig.indexOf('?'));
        }


        long longId;
        try {
            String parse = parseOrig;
            while (parse.length() < 16) {
                parse = "0" + parse;
            }
            String upper = parse.substring(0, 8);
            String lower = parse.substring(8, 16);

            Long u = Long.parseLong(upper, 16);
            Long l = Long.parseLong(lower, 16);
            longId = (u << 32) | l;

        } catch (NumberFormatException e) {
            try {
                // fallback to parse a decimal string
                longId = Long.parseLong(parseOrig);
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("Cannot convert '" + text
                        + "' into an identifier!");
            }
        }
        this.longHash = longId;
    }

    public long asLong() {
        return (this.longHash);
    }

    @Override
    public int hashCode() {
        return new Long(this.longHash).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().equals(obj.getClass()))
            return false;

        ItemId other = (ItemId) obj;
        return this.longHash == other.longHash;
    }

    public int compareTo(Object o) {
        if (o == null)
            return 1;
        if (!getClass().equals(o))
            return 1;

        ItemId other = (ItemId) o;
        if (this.longHash < other.longHash)
            return -1;
        if (this.longHash == other.longHash)
            return 0;
        return 1;
    }

    /**
     * <p>Produces the lexical string representation of this identifier, which
     * is the hex string (lower case) of the internal longHash.</p>
     * <p>
     * <p>This string you can use for <em>serializating</em> the indentifier
     * and later reconstruct it using the {@link #ItemId(String)} constructor.
     */
    public String toString() {
        String s = Long.toHexString(longHash);
        return s;
    }

    public Object getDelegate() {
        return this.delegate;
    }
}

