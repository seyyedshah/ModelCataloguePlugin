package uk.co.mc.core.util

import uk.co.mc.core.Relationship

/**
 * Wrapper used for easier marshalling of relations result lists
 */
class Relationships {
    String next
    String previous
    int total
    int page
    int offset
    List<Relationship> relationships
    String direction
}
